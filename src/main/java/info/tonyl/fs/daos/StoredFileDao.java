package info.tonyl.fs.daos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64.Encoder;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import info.tonyl.fs.config.Config;
import info.tonyl.fs.exceptions.FileExistsException;
import info.tonyl.fs.models.StoredFile;
import info.tonyl.fs.repos.StoredFileRepo;
import info.tonyl.fs.util.Util;

@Component
public class StoredFileDao {
	@Autowired
	private StoredFileRepo sfRepo;

	@Autowired
	private Config config;

	@Autowired
	private Encoder encoder;

	@Autowired
	private MessageDigest digest;

	public StoredFile saveNew(MultipartFile file, String path) throws IOException {
		StoredFile sf = new StoredFile();

		// Build partial and full paths, and make sure they're sensible (normalized)
		Path semiPath = FileSystems.getDefault().getPath(path).normalize();
		Path semiWithFilename = semiPath.resolve(file.getOriginalFilename()).normalize();
		Path actualPath = config.getFullDataPath().resolve(semiWithFilename).normalize();
		if (actualPath.toFile().exists()) {
			throw new FileExistsException("File at " + semiWithFilename.toString() + " already exists");
		}

		// Set all the simple fields
		sf.setName(semiWithFilename.getFileName().toString());
		sf.setPath(semiPath.toString());
		sf.setSize(file.getSize());
		sf.setStored(OffsetDateTime.now().toString());

		// Make the ID based on the full path of the file (so we can't overwrite)
		byte[] nameHash = digest.digest(semiWithFilename.toString().getBytes());
		sf.setId(encoder.encodeToString(nameHash).substring(0, config.getIdLength() - 1));

		// Write the actual file to the file system, and at the same time calculate the
		// hash
		InputStream in = file.getInputStream();
		byte[] buffer = new byte[config.getBufferSize()];
		File actualFile = actualPath.toFile();
		Util.createFile(actualFile);
		try (DigestOutputStream out = new DigestOutputStream(new FileOutputStream(actualPath.toFile()), digest)) {
			while (in.read(buffer) > 0) {
				out.write(buffer);
			}

			// Save the hash
			out.on(false);
			byte[] hash = out.getMessageDigest().digest();
			sf.setHash(encoder.encodeToString(hash));
		}

		// Store it in the database
		sf = sfRepo.save(sf);

		// Return what we just stored
		return sf;
	}

	public void deleteAll() {
		sfRepo.deleteAll();
		Util.deleteDirectory(config.getFullDataPath().toFile());
	}

	public boolean delete(String id) {
		StoredFile sf = get(id);
		boolean actuallyDeleted = false;
		if (sf != null) {
			actuallyDeleted = getActualPath(sf).toFile().delete();
		}
		sfRepo.deleteById(id);
		return actuallyDeleted;
	}

	public StoredFile get(String id) {
		Optional<StoredFile> osf = sfRepo.findById(id);
		if (osf.isPresent()) {
			return osf.get();
		} else {
			return null;
		}
	}

	public List<StoredFile> getAll() {
		List<StoredFile> result = new ArrayList<>();
		Iterable<StoredFile> it = sfRepo.findAll();
		for (StoredFile sf : it) {
			result.add(sf);
		}
		return result;
	}

	public Path getActualPath(StoredFile sf) {
		Path full = config.getFullDataPath().resolve(sf.getName());
		return full.normalize();
	}
}
