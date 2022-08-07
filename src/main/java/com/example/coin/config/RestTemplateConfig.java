package com.example.coin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ExtractingResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/**
 * @author Samuel Luo
 */
@Configuration
public class RestTemplateConfig {

    @Value("${app.rest.connection.timeout:3000}")
    private Integer connectionTimeout;

    @Bean
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new JavaScriptMessageConverter());
        //restTemplate.setErrorHandler(new  ExtractingResponseErrorHandler());

        return restTemplate;
    }

    public class JavaScriptMessageConverter extends AbstractJackson2HttpMessageConverter {

        private JavaScriptMessageConverter() {
            super(Jackson2ObjectMapperBuilder.json().build(), new MediaType("application","javascript"));
        }
    }

    public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return (response.getStatusCode().series() == CLIENT_ERROR
                    || response.getStatusCode().series() == SERVER_ERROR);
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
                    String httpBodyResponse = reader.lines().collect(Collectors.joining(""));
                    //log.error(httpBodyResponse);
                    //System.err.println(httpBodyResponse);
                } catch (IOException e) {
                    //log.error(e.getMessage());
                }
            }
        }
    }
}