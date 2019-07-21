package info.tonyl.fs.daos;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import info.tonyl.fs.models.StoredFile;

@Repository
public interface StoredFileRepository extends CrudRepository<StoredFile, UUID> {
}
