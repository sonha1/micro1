package vn.gtel.user.models.request.authentication;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
