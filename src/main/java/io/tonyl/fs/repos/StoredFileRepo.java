package io.tonyl.fs.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.tonyl.fs.models.StoredFile;

@Repository
public interface StoredFileRepo extends CrudRepository<StoredFile, String> {
}
