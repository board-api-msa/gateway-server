package me.junbyoung.gateway_server.filter;

import io.jsonwebtoken.Claims;
import me.junbyoung.gateway_server.util.JwtTokenUtil;
import me.junbyoung.gateway_server.util.RequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Order(2)
public class TokenValidationFilter implements GlobalFilter {

    //검증에 제외되는 요청들
    private static final List<RequestMatcher> EXCLUDED_PATHS = List.of(
            new RequestMatcher(HttpMethod.POST, "/api/users")
            , new RequestMatcher(HttpMethod.POST, "/api/users/login")
    );

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();
        RequestMatcher requestMatcher = new RequestMatcher(method, path);

        //지정된 요청 혹은 GET 요청은 검증하지않음.
        if (HttpMethod.GET.equals(method) || requestMatcher.match(EXCLUDED_PATHS)) {
            return chain.filter(exchange);
        }

        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        String jwtToken = getJwtFromRequest(requestHeaders);
        if (jwtToken != null && jwtTokenUtil.validateToken(jwtToken)) {
            Claims claims = jwtTokenUtil.getClaimsFromToken(jwtToken);
            long userId = Long.parseLong(claims.getSubject());
            exchange = exchange.mutate()
                    .request(builder -> {
                        builder.header("X-User-Id", String.valueOf(userId));
                    })
                    .build();
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    public String getJwtFromRequest(HttpHeaders requestHeaders) {
        String authorizationHeader = requestHeaders.getFirst("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
