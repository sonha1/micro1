package vn.gtel.user.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.gtel.user.models.userinfo.UserPrincipal;
import vn.gtel.user.services.RedisTokenService;
import vn.gtel.user.services.SecurityFilter;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final RedisTokenService tokenService;
    private final SystemPropertyConfiguration systemPropertyConfiguration;
    public SecurityConfig(RedisTokenService tokenService, SystemPropertyConfiguration systemPropertyConfiguration) {
        this.tokenService = tokenService;
        this.systemPropertyConfiguration = systemPropertyConfiguration;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        OncePerRequestFilter filter = new SecurityFilter(tokenService);



        return http
                // enable csrf protection with cookie
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // use stateless session management
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // accept all requests to static resources
                        .requestMatchers(systemPropertyConfiguration.getUriWhiteList()).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        // require authentication for all other requests
                        .anyRequest()
                        .authenticated()
                ).build();
    }

    private OAuth2AuthenticatedPrincipal introspectToken(String token) {
        UserPrincipal user = tokenService.validateToken(token);
        if (user == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token"), "Invalid token");
        }

        Map<String, Object> attributes = Map.of(
                "sub", user.getUsername(),
                "username", user.getUsername(),
                "authorities", user.getAuthorities()
        );
        return new DefaultOAuth2AuthenticatedPrincipal(attributes, user.getAuthorities());
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
