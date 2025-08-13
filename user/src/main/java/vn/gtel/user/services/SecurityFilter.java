package vn.gtel.user.services;

import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.gtel.user.utils.CONSTANT;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private RedisTokenService tokenService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String token = request.getHeader(CONSTANT.HEADER.HEADER_TOKEN);

            if (StringUtils.isEmpty(token)) {
                chain.doFilter(request, response);
                return;
            }

            token = token.replaceFirst("(?i)" + CONSTANT.HEADER.TOKEN_PREFIX, "");

//            Optional<LoginToken> tokenOptional = tokenQuery.getLoginToken(token);
//
//            if (tokenOptional.isEmpty()) {
//                chain.doFilter(request, response);
//                return;
//            }
//
//            CustomUserDetail authentication = new CustomUserDetail(tokenOptional.get(), null, null);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);


        } catch (Exception e) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);

            JsonObject rs = new JsonObject();
            rs.addProperty("code", 99);
            rs.addProperty("msg", "Not have access");
            response.getWriter().write(rs.toString());
        }


    }
}