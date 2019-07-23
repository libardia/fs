package info.tonyl.fs.config;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
@Configuration
public class Config {
	@Value("${fs.storage.id-length}")
	private Integer idLength;

	@Value("${fs.storage.buffer-size}")
	private Integer bufferSize;

	@Value("${fs.storage.base-path}")
	@Getter(AccessLevel.NONE)
	private Path basePath;

	public Path getBasePath() {
		String userDir = System.getProperty("user.home");
		return FileSystems.getDefault().getPath(userDir).resolve(basePath);
	}
}
