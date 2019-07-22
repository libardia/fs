package info.tonyl.fs.daos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Base64.Encoder;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import info.tonyl.fs.config.Config;
import info.tonyl.fs.models.StoredFile;
import info.tonyl.fs.repos.StoredFileRepo;

@Component
public class StoredFileDao {
	@Autowired
	StoredFileRepo sfRepo;

	@Autowired
	EntityManager entityManager;

	@Autowired
	Config config;

	@Autowired
	Encoder encoder;

	@Autowired
	MessageDigest digest;

	public StoredFile saveNew(MultipartFile file, String path) throws IOException {
		StoredFile sf = new StoredFile();

		// Build partial and full paths, and make sure they're sensible (normalized)
		Path semiPath = FileSystems.getDefault().getPath(path).normalize();
		Path fullPath = semiPath.resolve(file.getOriginalFilename()).normalize();
		Path actualPath = config.getBasePath().resolve(fullPath).normalize();

		// Set all the simple fields
		sf.setName(fullPath.getFileName().toString());
		sf.setPath(semiPath.toString());
		sf.setSize(file.getSize());

		// Make the ID based on the full path of the file (so we can't overwrite)
		byte[] nameHash = digest.digest(fullPath.toString().getBytes());
		sf.setId(encoder.encodeToString(nameHash).substring(0, config.getIdLength() - 1));

		// Store it in the database
		sf = sfRepo.save(sf);

		// Write the actual file to the filesystem
		InputStream in = file.getInputStream();
		byte[] buffer = new byte[config.getBufferSize()];
		File actualFile = actualPath.toFile();
		actualFile.getParentFile().mkdirs();
		if (!actualFile.createNewFile()) {
			throw new IOException("File already exists");
		}
		try (FileOutputStream out = new FileOutputStream(actualPath.toFile())) {
			while (in.read(buffer) > 0) {
				out.write(buffer);
			}
		}

		// Return what we just stored
		return sf;
	}

	public Path getActualPath(StoredFile sf) {
		Path base = config.getBasePath();
		Path full = base.resolve(sf.getPath()).resolve(sf.getName());
		return full.normalize();
	}
}
