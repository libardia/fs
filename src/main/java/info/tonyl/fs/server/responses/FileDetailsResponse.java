package info.tonyl.fs.server.responses;

import info.tonyl.fs.server.models.StoredFile;
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
