package io.tonyl.fs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Profile("dev")
public class SecurityConfigDev extends WebSecurityConfigurerAdapter {
	@Override
	public void configure(WebSecurity web) {
		// No security for any request
		web.ignoring().anyRequest();
	}
}
