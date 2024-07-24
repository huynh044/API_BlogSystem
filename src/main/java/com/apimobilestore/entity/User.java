package com.apimobilestore.entity;

import java.time.Instant;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String uid;
    
    String firstname;
    String lastname;
    String username;
    String password;
    Instant createdAt;
    Instant updatedAt;
    
    @ManyToMany
    @JoinTable(
        name = "user_role", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles;
    
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
