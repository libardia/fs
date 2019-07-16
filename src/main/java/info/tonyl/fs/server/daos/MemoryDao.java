package info.tonyl.fs.server.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import info.tonyl.fs.server.models.Memory;

public interface MemoryDao extends JpaRepository<Memory, Long> {
	Memory findOne(Long id);
}
