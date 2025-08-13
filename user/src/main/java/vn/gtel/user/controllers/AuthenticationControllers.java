package vn.gtel.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.gtel.user.models.request.authentication.LoginRequest;
import vn.gtel.user.models.response.authentication.LoginResponse;
import vn.gtel.user.services.AuthenticationService;
import vn.gtel.user.utils.ApplicationException;

@RestController
public class AuthenticationControllers {
    @Autowired
    AuthenticationService authenticationService;


    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws ApplicationException {
        return authenticationService.login(loginRequest);
    }
}
