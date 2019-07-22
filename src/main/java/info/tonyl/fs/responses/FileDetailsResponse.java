package info.tonyl.fs.responses;

import info.tonyl.fs.models.StoredFile;
import lombok.Data;

@Data
public class FileDetailsResponse {
	public FileDetailsResponse(StoredFile sf) {
		name = sf.getName();
		size = sf.getSize();
	}

	private String name;
	private Long size;
}
