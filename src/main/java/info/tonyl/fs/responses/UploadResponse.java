package info.tonyl.fs.responses;

import lombok.Data;

@Data
public class UploadResponse {
	public UploadResponse(String id) {
		message = "Successfully uploaded file";
		this.id = id;
	}

	private String id;
	private String message;
}
