package vn.gtel.user.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.gtel.user.models.request.user.CreateUserRequest;
import vn.gtel.user.services.UserService;
import vn.gtel.user.utils.ApplicationException;

@RestController
@RequestMapping("/public/api/user")
@Slf4j
public class UserControllers {
    @Autowired
    private UserService userService;

    @PostMapping
    public void createUser(@RequestBody CreateUserRequest request) throws ApplicationException {
        userService.createUser(request);
    }


}