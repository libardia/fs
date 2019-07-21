package info.tonyl.fs.responses;

import java.util.UUID;

import lombok.Data;

@Data
public class UploadResponse {
	public UploadResponse(UUID id) {
		message = "Successfully stored file in database";
		this.id = id;
	}

	private String message;
	private UUID id;
}
