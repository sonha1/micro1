package vn.gtel.user.redis.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("token")
@Data
public class Token {

    @Id
    private String token;

    @Indexed
    private Long userId;

    @TimeToLive
    private long timeToLive;
}
