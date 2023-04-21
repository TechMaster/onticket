package vn.techmaster.onticket.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private String resetPasswordToken;
    
    public void setResetPasswordToken() {
        String token = UUID.randomUUID().toString();
        this.resetPasswordToken = token;
    }

    private LocalDateTime resetPasswordTokenExpiration;
    
    public void setResetPasswordTokenExpiration() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusHours(24);
        this.resetPasswordTokenExpiration = expirationTime;
    }
}