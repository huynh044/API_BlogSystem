package com.apimobilestore.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

@TestConfiguration
public class Configuration {
	@Bean
	  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    return http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
	          .csrf(AbstractHttpConfigurer::disable)
	          .authorizeHttpRequests(
	                authorize -> authorize.anyRequest().permitAll()
	          )
	          .build();
	  }

	  CorsConfigurationSource corsConfigurationSource() {
		    CorsConfiguration configuration = new CorsConfiguration();
		    configuration.addAllowedHeader("*");
		    configuration.addAllowedOrigin("*");
		    configuration.addAllowedMethod("*");
		    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		    source.registerCorsConfiguration("/**", configuration);
	    return source;
	  }
}
