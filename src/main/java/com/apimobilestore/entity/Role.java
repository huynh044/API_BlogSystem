package com.apimobilestore.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
public class Role {
	@Id
	String name;
	
	String description;
	
	/*
	 * @ManyToMany(fetch = FetchType.EAGER)
	 * 
	 * @JoinTable( name = "role_permissions", joinColumns = @JoinColumn(name =
	 * "role_name"), inverseJoinColumns = @JoinColumn(name = "permission_name") )
	 * Set<Permission> permissions;
	 */
}
