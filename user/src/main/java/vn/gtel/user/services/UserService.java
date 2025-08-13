package vn.gtel.user.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.gtel.user.domains.UserDomain;
import vn.gtel.user.entities.UserEntity;
import vn.gtel.user.models.request.user.CreateUserRequest;
import vn.gtel.user.utils.ApplicationException;
import vn.gtel.user.utils.ERROR_CODE;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserDomain userDomain;

    private final PasswordEncoder passwordEncoder;


    public void createUser(CreateUserRequest request){
        log.info("Creating user with name {} START", request.getUsername());

        this.validateRequestCreateUser(request);

        Optional<UserEntity> userOtp = userDomain.findByUsername(request.getUsername());


        if (userOtp.isPresent()) {
            log.info("User with name {} already exists", request.getUsername());
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "User with name " + request.getUsername() + " already exists");
        }

        UserEntity user = new UserEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userDomain.save(user);
        log.info("Creating user with name {} SUCCESS", request.getUsername());
    }


    private void validateRequestCreateUser(CreateUserRequest request) throws ApplicationException {

        if (StringUtils.isBlank(request.getUsername())) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "Username is required");
        }

        if (StringUtils.isBlank(request.getPassword())) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "Password is required");
        }


        if (StringUtils.isBlank(request.getFullName())) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "Fullname is required");
        }
    }
}
