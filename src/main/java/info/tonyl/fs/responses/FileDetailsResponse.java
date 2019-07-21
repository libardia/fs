package info.tonyl.fs.responses;

import info.tonyl.fs.models.StoredFile;
import lombok.Data;

@Data
public class FileDetailsResponse {
	public FileDetailsResponse(StoredFile sf) {
		name = sf.getName();
		type = sf.getType();
		size = sf.getSize();
	}

	private String name;
	private String type;
	private Long size;
}
