package vn.gtel.user.entities;

import jakarta.persistence.*;
import lombok.Data;
import vn.gtel.user.models.request.user.CreateUserRequest;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String email;

    @Column(name = "phone")
    private String phoneNumber;


    @Column(name = "fullname")
    private String fullName;

    private int status;

    @Column(name = "ROLES")
    private String roles;

    @Column(name = "SCOPES")
    private String scopes;

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    public UserEntity() {

    }

    public UserEntity(CreateUserRequest request){
        this.username = request.getUsername();
        this.email = request.getEmail();
        this.phoneNumber = request.getPhone();
        this.fullName = request.getFullName();
    }
}
