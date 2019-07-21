package info.tonyl.fs.daos;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import info.tonyl.fs.models.Memory;

@Repository
public interface MemoryRepository extends CrudRepository<Memory, Long> {
	@SuppressWarnings("unchecked")
	public Memory save(Memory m);

	public Memory getByKey(UUID key);
}
