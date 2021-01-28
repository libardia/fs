package info.tonyl.fs.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import info.tonyl.fs.models.StoredFile;

@Repository
public interface StoredFileRepo extends CrudRepository<StoredFile, String> {
}