package vn.gtel.user.models.userinfo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
@Data
public class UserRole implements GrantedAuthority {

    private String role;

    public UserRole(String role) {
        this.role = role;
    }
    @Override
    public String getAuthority() {
        return role;
    }
}
