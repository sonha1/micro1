package vn.gtel.user.redis.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("client")
@Data
public class Client {
    @Id
    private String id;
    private String secret;

    public Client(String id, String secret) {
        this.id = id;
        this.secret = secret;
    }
}
