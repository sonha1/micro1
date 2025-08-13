package vn.gtel.user.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.gtel.user.redis.entities.UserInfo;

public interface UserCacheRepository extends CrudRepository<UserInfo, Long> {
}
