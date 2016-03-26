package com.gregdm.javart.service.art.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * Created by Greg on 26/03/2016.
 */
@Configuration
@EnableConfigurationProperties(ArtApiProperties.class)
public class ArtApiConfig {

    @Inject
    private ArtApiProperties properties;

    @Bean
    public RestTemplate getArtApiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ArtApiErrorHandler());
        return restTemplate;
    }

    @Bean
    public HttpHeaders getArtApiHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("ApiKey",  properties.getApiKey());
        return headers;
    }
}
