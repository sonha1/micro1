package vn.gtel.user.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.gtel.user.domains.UserDomain;
import vn.gtel.user.entities.UserEntity;
import vn.gtel.user.models.request.authentication.LoginRequest;
import vn.gtel.user.models.response.authentication.LoginResponse;
import vn.gtel.user.models.userinfo.UserPrincipal;
import vn.gtel.user.models.userinfo.UserRole;
import vn.gtel.user.utils.ApplicationException;
import vn.gtel.user.utils.ERROR_CODE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationService {

    private final RedisTokenService redisTokenService;

    private final UserDomain userDomain;

    private final PasswordEncoder passwordEncoder;
    public LoginResponse login(LoginRequest loginRequest) {


        log.info("login from user: {}", loginRequest.getUsername());
        Optional<UserEntity> userOtp = userDomain.findByUsername(loginRequest.getUsername());

        if (userOtp.isEmpty()) {
            throw new ApplicationException(ERROR_CODE.PASSWORD_NOT_MATCH , HttpStatus.UNAUTHORIZED);
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), userOtp.get().getPassword())) {
            throw new ApplicationException(ERROR_CODE.PASSWORD_NOT_MATCH , HttpStatus.UNAUTHORIZED);
        }
        UserEntity user = userOtp.get();
        String token = UUID.randomUUID().toString();

        List<GrantedAuthority> roles = new ArrayList<>();

        if (StringUtils.isNotBlank(user.getRoles())){
            for (String role : user.getRoles().split(",")){
                roles.add(new UserRole(role));
            }
        }

        UserPrincipal userPrincipal = new UserPrincipal(user.getUsername() , roles);
        userPrincipal.setId(user.getId());
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setEmail(user.getEmail());
        userPrincipal.setFullName(user.getFullName());
        userPrincipal.setScopes(user.getScopes());
        userPrincipal.setPhoneNumber(user.getPhoneNumber());


        redisTokenService.saveService(token, userPrincipal, 10000L);


        LoginResponse response = new LoginResponse();

        response.setToken(token);

        return response;
    }
}
