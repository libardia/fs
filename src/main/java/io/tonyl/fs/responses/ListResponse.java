package io.tonyl.fs.responses;

import java.util.List;

import io.tonyl.fs.models.StoredFile;
import lombok.Data;

@Data
public class ListResponse {
	public ListResponse(List<StoredFile> files) {
		this.files = files;
	}

	private List<StoredFile> files;
}
