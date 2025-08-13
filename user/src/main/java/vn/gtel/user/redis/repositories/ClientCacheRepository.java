package vn.gtel.user.redis.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.gtel.user.redis.entities.Client;

public interface ClientCacheRepository extends CrudRepository<Client, String> {
}
