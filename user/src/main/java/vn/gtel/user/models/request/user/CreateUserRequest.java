package vn.gtel.user.models.request.user;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String email;
    private String phone;
    private String password;
    private String fullName;
}
