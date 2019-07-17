package info.tonyl.fs.server.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
public class Memory {
	@Id
	@GeneratedValue
	@Setter(AccessLevel.NONE)
	Long id;
	@Column(unique = true, updatable = false)
	@Setter(AccessLevel.NONE)
	UUID key;
	String message;

	public Memory() {
		key = UUID.randomUUID();
	}
}
