package info.tonyl.fs.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.Resource;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
public class StoredFile {
	@Id
	@Setter(AccessLevel.NONE)
	private String id;
	private String name;
	private Long size;

	@Lob
	private Resource data;

	public void generateOwnId() throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		id = Base64.encodeBase64String(digest.digest(name.getBytes())).substring(0, 9);
	}
}
