package info.tonyl.fs.models;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class StoredFile {
	@Id
	private String id;
	private String path;
	private String name;
	private Long size;
	private String stored;
}
