package com.gtel.warehouse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final SystemPropertyConfiguration systemPropertyConfiguration;


    public SecurityConfig(SystemPropertyConfiguration systemPropertyConfiguration) {

        this.systemPropertyConfiguration = systemPropertyConfiguration;
    }

    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector(@Value("${spring.security.oauth2.resourceserver.opaque-token.introspection-uri}") String url,
                                                           @Value("${spring.security.oauth2.resourceserver.opaque-token.client-id}") String clientId,
                                                           @Value("${spring.security.oauth2.resourceserver.opaque-token.client-secret}") String secret) {
        return new CustomAuthoritiesOpaqueTokenIntrospector(url, clientId, secret);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // enable csrf protection with cookie
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // use stateless session management
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // accept all requests to static resources
                        .requestMatchers(systemPropertyConfiguration.getUriWhiteList()).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        // require authentication for all other requests
                        .anyRequest()
                        .authenticated()

                )
                // OAuth2 Resource Server: Spring tự gọi /introspect
                .oauth2ResourceServer(oauth2 -> oauth2
                        .opaqueToken(Customizer.withDefaults())
                )
                .build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(systemPropertyConfiguration.getCors().allowCredentials());
        configuration.setAllowedHeaders(systemPropertyConfiguration.getCors().allowedHeader());
        configuration.setAllowedOrigins(systemPropertyConfiguration.getCors().allowedOriginPattern());
        configuration.setAllowedMethods(systemPropertyConfiguration.getCors().allowedMethods());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
