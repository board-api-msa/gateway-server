package me.junbyoung.gateway_server.util;


import org.springframework.http.HttpMethod;

import java.util.List;

public class RequestMatcher {
    HttpMethod method;
    String uri;

    public RequestMatcher(HttpMethod method, String uri){
        this.method = method;
        this.uri = uri;
    }

    public boolean match(List<RequestMatcher> list){
        for (RequestMatcher requestMatcher : list) {
            if(this.uri.equals(requestMatcher.uri) && this.method.equals(requestMatcher.method)){
                return true;
            }
        }
        return false;
    }
}
