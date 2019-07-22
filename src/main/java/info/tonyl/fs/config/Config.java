package info.tonyl.fs.config;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class Config {
	@Value("${fs.storage.base-path}")
	private Path basePath;

	@Value("${fs.storage.id-length}")
	private Integer idLength;

	@Value("${fs.storage.buffer-size}")
	private Integer bufferSize;
}
