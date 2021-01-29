package io.tonyl.fs.config;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

@Getter
@Configuration
@PropertySource(value = "file:${fs.storage.base-path}/authentication.yml", factory = YmlPropertySourceFactory.class)
public class Config {
	@Value("${fs.storage.download-url}")
	private String downloadUrl;

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

	@Value("${fs.storage.profile-path}")
	private Path profilePath;

	@Value("${fs.storage.data-path}")
	private Path dataPath;

	public Path getFullProfilePath() {
		return basePath.resolve(profilePath).normalize();
	}

	public Path getFullDataPath() {
		return basePath.resolve(profilePath).resolve(dataPath).normalize();
	}
}