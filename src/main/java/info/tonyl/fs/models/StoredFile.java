package info.tonyl.fs.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Data;

@Data
@Entity
public class StoredFile {
	@Id
	private String id;
	private String name;
	private Long size;
	private String hash;

	@Lob
	private byte[] data;
}
