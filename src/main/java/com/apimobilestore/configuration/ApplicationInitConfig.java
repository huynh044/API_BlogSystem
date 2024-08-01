package com.apimobilestore.configuration;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.apimobilestore.entity.Role;
import com.apimobilestore.entity.User;
import com.apimobilestore.enums.Roles;
import com.apimobilestore.exception.AppException;
import com.apimobilestore.exception.ErrorCode;
import com.apimobilestore.repository.RoleRepository;
import com.apimobilestore.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
	PasswordEncoder passwordEncoder;
	
	@Value("${account.admin.username}")
	@NonFinal
	protected String ADMIN_USERNAME;
	
	@Value("${account.admin.password}")
	@NonFinal
	protected String ADMIN_PASSWORD;
	
	@Bean
	@ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
	ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
		log.info("Initializing application.....");
		
        return args -> {
        	Role newRole = new Role();
        	for (Roles role : Roles.values()) {
				if(roleRepository.findById(role.name()).isEmpty()) {
					newRole.setName(role.name());
					roleRepository.save(newRole);
				}
			}
        	
            if (userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {  
                Role adminRole = roleRepository.findById(Roles.ADMIN.name())
                		.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
                Role userRole = roleRepository.findById(Roles.USER.name())
                		.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
                Role authorRole = roleRepository.findById(Roles.AUTHOR.name())
                		.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
                Role viewerRole = roleRepository.findById(Roles.VIEWER.name())
                		.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

                var roles = new HashSet<Role>();
                roles.add(adminRole);
                roles.add(userRole);
                roles.add(authorRole);
                roles.add(viewerRole);
                
                User user = User.builder()
                        .username(ADMIN_USERNAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
	}
}
