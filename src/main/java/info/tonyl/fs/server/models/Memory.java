package info.tonyl.fs.server.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Memory {
	@Id
	@GeneratedValue
	Long id;
	String message;
}
