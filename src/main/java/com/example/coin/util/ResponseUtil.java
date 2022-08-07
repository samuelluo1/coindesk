package com.example.coin.util;

import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

/**
 * @author jhipster
 */
public class ResponseUtil {
    public static <T> ResponseEntity<T> wrapOrNotFound(Optional<T> maybeResponse) {
        return wrapOrNotFound(maybeResponse, null);
    }

    private static <T> ResponseEntity<T> wrapOrNotFound(Optional<T> maybeResponse, HttpHeaders header) {
        return (ResponseEntity)maybeResponse.map((response) -> {
            return ((BodyBuilder)ResponseEntity.ok().headers(header)).body(response);
        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }
}