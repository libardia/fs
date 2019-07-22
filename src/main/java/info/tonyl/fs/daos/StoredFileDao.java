package info.tonyl.fs.daos;

import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.util.Base64;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import info.tonyl.fs.models.StoredFile;
import info.tonyl.fs.repos.StoredFileRepository;

@Component
public class StoredFileDao {
	@Autowired
	StoredFileRepository sfRepo;

	@Autowired
	EntityManager entityManager;

	public StoredFile saveNew(MultipartFile file) throws NoSuchAlgorithmException, IOException {
		StoredFile sf = new StoredFile();

		// Set the easy properties
		sf.setName(file.getOriginalFilename());
		sf.setSize(file.getSize());

		// Use LobHelper from Hibernate to get a blob from a stream
		Session session = entityManager.unwrap(Session.class);
		Blob data = session.getLobHelper().createBlob(file.getInputStream(), file.getSize());
		sf.setData(data);

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
		String hashB64 = Base64.getUrlEncoder().encodeToString(hashed);
		sf.setHash(hashB64);

		// Hash again, salting it with the name.
		hashed = digest.digest((hashB64 + sf.getName()).getBytes());
		String id = Base64.getUrlEncoder().encodeToString(hashed).substring(0, 9);
		sf.setId(id);

		// Finally, save it. ALL OF THIS is done so the ID can be the same between files
		// with the same contents AND name. Only the last 10 digits are used as the ID.
		sf = sfRepo.save(sf);

		return sf;
	}
}
