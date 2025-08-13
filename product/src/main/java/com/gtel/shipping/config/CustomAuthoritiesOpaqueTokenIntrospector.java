package com.gtel.shipping.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomAuthoritiesOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    public CustomAuthoritiesOpaqueTokenIntrospector(String url, String clientId, String secret) {
        this.delegate =  new SpringOpaqueTokenIntrospector(url, clientId, secret);
    }

    private OpaqueTokenIntrospector delegate;


    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
        return new DefaultOAuth2AuthenticatedPrincipal(
                principal.getName(), principal.getAttributes(), extractAuthorities(principal));
    }

    private Collection<GrantedAuthority> extractAuthorities(OAuth2AuthenticatedPrincipal principal) {
        List<String> scopes = principal.getAttribute("authorities");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(principal.getAuthorities())) {
            for (GrantedAuthority p : principal.getAuthorities()) {
                authorities.add(p);
            }
        }


        if (!CollectionUtils.isEmpty(scopes)) {
            authorities.addAll(scopes.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));
        }


        return authorities;
    }
}
