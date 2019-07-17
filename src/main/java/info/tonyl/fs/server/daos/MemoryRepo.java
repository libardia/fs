package info.tonyl.fs.server.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import info.tonyl.fs.server.models.Memory;

@Repository
public interface MemoryRepo extends CrudRepository<Memory, Long> {
	@SuppressWarnings("unchecked")
	public Memory save(Memory m);

	public Memory getById(Long id);
}
