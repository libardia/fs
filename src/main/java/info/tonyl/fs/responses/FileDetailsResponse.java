package info.tonyl.fs.responses;

import info.tonyl.fs.models.StoredFile;
import lombok.Data;

@Data
public class FileDetailsResponse {
	public FileDetailsResponse(StoredFile sf) {
		id = sf.getId();
		name = sf.getName();
		size = sf.getSize();
		hash = sf.getHash();
	}

	private String id;
	private String name;
	private Long size;
	private String hash;
}
