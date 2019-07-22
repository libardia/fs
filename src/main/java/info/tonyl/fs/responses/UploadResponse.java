package info.tonyl.fs.responses;

import lombok.Data;

@Data
public class UploadResponse {
	public UploadResponse(String id) {
		message = "Successfully stored file in database";
		this.id = id;
	}

	private String id;
	private String message;
}
