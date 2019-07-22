package info.tonyl.fs.responses;

import java.util.ArrayList;
import java.util.List;

import info.tonyl.fs.models.StoredFile;
import lombok.Data;

@Data
public class ListResponse {
	public ListResponse(Iterable<StoredFile> filesIter) {
		files = new ArrayList<>();

		for (StoredFile sf : filesIter) {
			files.add(sf);
		}
	}

	private List<StoredFile> files;
}
