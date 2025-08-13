package vn.gtel.user.domains;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import vn.gtel.user.entities.UserEntity;
import vn.gtel.user.repositories.UserRepository;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserDomain {

    private final UserRepository  userRepository;

    public Optional<UserEntity> findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    public UserEntity  save(UserEntity user) {
       return  userRepository.save(user);
    }
}
