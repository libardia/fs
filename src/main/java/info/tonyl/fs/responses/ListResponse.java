package info.tonyl.fs.responses;

import java.util.List;

import info.tonyl.fs.models.StoredFile;
import lombok.Data;

@Data
public class ListResponse {
	public ListResponse(List<StoredFile> files) {
		this.files = files;
	}

	private List<StoredFile> files;
}
