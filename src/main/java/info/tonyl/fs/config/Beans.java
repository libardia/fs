package info.tonyl.fs.config;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {
	@Bean
	Base64 base64() {
		return new Base64(true);
	}
}
