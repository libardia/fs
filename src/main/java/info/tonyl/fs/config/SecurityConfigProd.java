package info.tonyl.fs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
@Profile("prod")
public class SecurityConfigProd extends WebSecurityConfigurerAdapter {
	@Autowired
	Config config;

	@Autowired
	PasswordEncoder encoder;

//	@Bean
//	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//		http.redirectToHttps();
//		return http.build();
//	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// Use basic authentication
		http.httpBasic();
		// Require HTTPS
		http.requiresChannel().anyRequest().requiresSecure();
		// Allow anyone to connect to /download/* without authenticating, but any other
		// endpoint should be secured
		http.authorizeRequests().antMatchers("/download/*").permitAll().anyRequest().authenticated();
		// Never use a session -- credentials are required every call
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Disable Cross Site Request Forgery protection
		http.csrf().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// Take the username and password from the properties to use for the basic
		// authentication. The password is set using an encoder for added security.
		auth.inMemoryAuthentication().withUser(config.getUsername()).password(encoder.encode(config.getPassword()))
				.roles("USER");
	}
}
