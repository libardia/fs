package info.tonyl.fs.server.models;

import java.util.UUID;

import lombok.Data;

@Data
public class UploadResponse {
	private Integer status;
	private String message;
	private UUID id;
}
