package info.tonyl.fs.server.daos;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import info.tonyl.fs.server.models.StoredFile;

@Repository
public interface StoredFileRepository extends CrudRepository<StoredFile, UUID> {
}
