package vn.gtel.user.domains;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import vn.gtel.user.redis.entities.Client;
import vn.gtel.user.redis.repositories.ClientCacheRepository;
import vn.gtel.user.repositories.ClientRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class ClientDomain {
    private ClientRepository clientRepository;
    private ClientCacheRepository redisClientRepository;

    public ClientDomain(ClientRepository clientRepository, ClientCacheRepository redisClientRepository) {
        this.clientRepository = clientRepository;
        this.redisClientRepository = redisClientRepository;
    }

    private Cache<String, String> CACHE = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();


    @PostConstruct
    public void init() {
        redisClientRepository.deleteAll();
        clientRepository.findAll().forEach(c -> {

            Client client = new Client(c.getId(), c.getSecret());

            redisClientRepository.save(client);
        });
    }

    public boolean validateClientAndSecret(String clientId, String secretKey) {

        String secret = CACHE.getIfPresent(clientId);

        if (secret == null) {
            Optional<Client> client = redisClientRepository.findById(clientId);


            CACHE.put(clientId, client.isPresent() ? client.get().getSecret() : "NONE");
            secret = CACHE.getIfPresent(clientId);

        }

        if (secret.equals("NONE")) {
            return false;
        }

        return secret.equals(secretKey);
    }



    public boolean isValidClient(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Basic ")) return false;
        String base64 = authHeader.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
        String[] parts = decoded.split(":");
       // return "my-client".equals(parts[0]) && "secret".equals(parts[1]);

        return this.validateClientAndSecret(parts[0], parts[1]);
    }
}
