package vn.gtel.user.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.gtel.user.redis.entities.Token;

public interface TokenRepository extends CrudRepository<Token, String> {
}
