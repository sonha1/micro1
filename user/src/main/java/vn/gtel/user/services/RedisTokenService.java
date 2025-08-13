package vn.gtel.user.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.gtel.user.models.userinfo.UserPrincipal;
import vn.gtel.user.redis.entities.Token;
import vn.gtel.user.redis.entities.UserInfo;
import vn.gtel.user.redis.repositories.TokenRepository;
import vn.gtel.user.redis.repositories.UserCacheRepository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service

public class RedisTokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserCacheRepository userCacheRepository;


    private Cache<String, Long> CACHE = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .maximumSize(100000)
            .build();

    private Cache<Long, UserPrincipal> CACHE_USER = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .maximumSize(50000)
            .build();


    public UserPrincipal validateToken(String token) {
        long userId = 0L;
        if (CACHE.getIfPresent(token) != null) {
            userId = CACHE.getIfPresent(token);


        } else {
            Optional<Token> optionalToken = tokenRepository.findById(token);

            if (optionalToken.isEmpty()) {
                return null;
            }

            userId = optionalToken.get().getUserId();

            if (optionalToken.get().getTimeToLive() > 30) {
                CACHE.put(token, userId);
            }
        }

        if (CACHE_USER.getIfPresent(userId) != null) {
            return CACHE_USER.getIfPresent(userId);
        }

        Optional<UserInfo> userInfo = userCacheRepository.findById(userId);

        if (userInfo.isPresent()) {
            CACHE_USER.put(userId, userInfo.get().getPrincipal());
            return userInfo.get().getPrincipal();
        }

        return null;
    }

    public void saveService(String token, UserPrincipal user, long timeToLive) {
        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setUserId(user.getId());
        tokenEntity.setTimeToLive(timeToLive);
        tokenRepository.save(tokenEntity);

        UserInfo userInfo = new UserInfo();
        userInfo.setPrincipal(user);
        userInfo.setId(user.getId());
        userCacheRepository.save(userInfo);
    }
}
