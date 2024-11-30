package me.junbyoung.gateway_server.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class TrailingSlashFilter implements GlobalFilter {

    /*
        TrailingSlash 을 활성화해주는 글로벌 필터
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.endsWith("/") && path.length() > 1) {
            String newPath = path.substring(0, path.length() - 1);
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(exchange.getRequest().mutate().path(newPath).build())
                    .build();
            return chain.filter(mutatedExchange);
        }

        return chain.filter(exchange);
    }
}
