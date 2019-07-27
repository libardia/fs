package info.tonyl.fs.config;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

@Getter
@Configuration
@PropertySource("file:${fs.storage.base-path}/authentication.yml")
public class Config {
	@Value("${fs.auth.username}")
	private String username;

	@Value("${fs.auth.password}")
	private String password;

	@Value("${fs.storage.id-length}")
	private Integer idLength;

	@Value("${fs.storage.buffer-size}")
	private Integer bufferSize;

	@Value("${fs.storage.base-path}")
	private Path basePath;

	@Value("${fs.storage.data-path}")
	private Path dataPath;
}
