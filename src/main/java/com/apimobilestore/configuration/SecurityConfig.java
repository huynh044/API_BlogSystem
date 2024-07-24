package com.apimobilestore.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	private final String[] PUBLIC_ENDPOINTS = {
            "/user/create", "/auth/login", "/auth/introspect", "/auth/logout", "/auth/refresh","/swagger-ui.html"
    };
	
	@Autowired
	JwtDecode jwtDecode;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS)
																.permitAll()
																.anyRequest()
																.authenticated());
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		httpSecurity.oauth2ResourceServer(oath2 -> oath2.jwt(jwtConfigurer -> jwtConfigurer
				.decoder(jwtDecode)
				.jwtAuthenticationConverter(jwtAuthenticationConverter()))
				.authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
		return httpSecurity.build();
	}
	
	 @Bean
	    JwtAuthenticationConverter jwtAuthenticationConverter() {
	        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
	        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

	        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
	        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

	        return jwtAuthenticationConverter;
	    }
	 
	 @Bean
	    CorsFilter corsFilter(){
	        CorsConfiguration corsConfiguration = new CorsConfiguration();

	        corsConfiguration.addAllowedOrigin("*");
	        corsConfiguration.addAllowedMethod("*");
	        corsConfiguration.addAllowedHeader("*");

	        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
	        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

	        return new CorsFilter(urlBasedCorsConfigurationSource);
	    }
	
	// generate encode password
    @Bean
    @Lazy
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
    
    
}
