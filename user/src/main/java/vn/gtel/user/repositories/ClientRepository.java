package vn.gtel.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.gtel.user.entities.ClientEntity;

public interface ClientRepository extends JpaRepository<ClientEntity, String> {
}
