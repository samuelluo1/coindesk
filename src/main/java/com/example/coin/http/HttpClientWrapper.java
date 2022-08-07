package com.example.coin.http;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;


/**
 * @author Samuel Luo
 */
@Component
public class HttpClientWrapper {
    public <T> T getCoinDesk(String url, Class<T> clazz) {
        return (T) url;
    }
}
