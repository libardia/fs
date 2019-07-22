package info.tonyl.fs.daos;

import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import info.tonyl.fs.models.StoredFile;
import info.tonyl.fs.repos.StoredFileRepository;

@Component
public class StoredFileDao {
	@Autowired
	StoredFileRepository sfRepo;

	public StoredFile saveNew(MultipartFile file) throws NoSuchAlgorithmException, IOException {
		StoredFile sf = new StoredFile();

		// Set the easy properties
		sf.setName(file.getOriginalFilename());
		sf.setSize(file.getSize());

		// TODO: Set the blob
		sf.setData(file.getBytes());

		// Hash the file contents
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashed;
		// Try-with-resources; we can be sure dStream is closed.
		try (DigestInputStream dStream = new DigestInputStream(file.getInputStream(), digest)) {
			// 4KB at a time, read out the contents so the digest is updated.
			byte[] buffer = new byte[4096];
			int read;
			do {
				read = dStream.read(buffer);
			} while (read > 0);
			// Retrieve the calculated digest.
			hashed = dStream.getMessageDigest().digest();
		}

		// Save the hash string as base 64
		String hashB64 = Base64.getEncoder().encodeToString(hashed);
		sf.setHash(hashB64);

		// Hash again, salting it with the name
		hashed = digest.digest((hashB64 + sf.getName()).getBytes());

		// Finally, save it. ALL OF THIS is done so the ID can be the same between files
		// with the same contents AND name. Only the last 10 digits are used as the ID.
		String id = Base64.getEncoder().encodeToString(hashed).substring(0, 9);
		sf.setId(id);

		sf = sfRepo.save(sf);

		return sf;
	}
}
