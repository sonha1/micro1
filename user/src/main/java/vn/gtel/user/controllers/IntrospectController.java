package vn.gtel.user.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.gtel.user.domains.ClientDomain;
import vn.gtel.user.models.userinfo.UserPrincipal;
import vn.gtel.user.services.RedisTokenService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class IntrospectController {

    @Autowired
    private RedisTokenService tokenService;

    @Autowired
    private ClientDomain clientDomain;

    @PostMapping(value = "/introspect", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> introspect(@RequestParam MultiValueMap<String,String> paramMap , @RequestHeader Map<String,String> headers) {

        String token = paramMap.getFirst("token");
        String authHeader = headers.get("authorization");

        if (!clientDomain.isValidClient(authHeader )) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserPrincipal userInfo = tokenService.validateToken(token);
        if (userInfo == null) {
            return ResponseEntity.ok(Map.of("active", false));
        }
        List<String> roles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userInfo.getAuthorities())){
            for (GrantedAuthority authority : userInfo.getAuthorities()) {
                roles.add(authority.getAuthority());
            }
        }

        return ResponseEntity.ok(Map.of(
                "active", true,
                "sub", userInfo.getUsername(),
                "username", userInfo.getUsername(),
                "scope", StringUtils.isBlank(userInfo.getScopes()) ? "USER" :userInfo.getScopes() ,
                "authorities", roles
        ));
    }


}