package com.gregdm.javart.repository.art;

import com.gregdm.javart.domain.art.Work;
import com.gregdm.javart.domain.art.wrapper.ResponseWrapper;
import com.gregdm.javart.service.art.config.ArtApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by Greg on 26/03/2016.
 */
@Repository
@EnableConfigurationProperties(ArtApiProperties.class)
public class WorkRepository {

    @Inject
    private ArtApiProperties properties;

    @Inject
    protected HttpHeaders artApiHeader;

    @Inject
    protected RestTemplate artApiRestTemplate;

    public Optional<Work> get(String id) {
        try {
            String url = properties.getUrl() + "works/" + id;

            HttpEntity<String> headers = new HttpEntity<>(artApiHeader);
            ResponseEntity<ResponseWrapper> responseEntity = artApiRestTemplate.exchange(
                url,
                HttpMethod.GET,
                headers,
                ResponseWrapper.class
            );
            ResponseWrapper hits = responseEntity.getBody();
            if(hits != null && !hits.isTimeOut() && hits.getHits().getTotal() > 0) {
                return Optional.of(hits.getHits().getHits().get(0).getWork());
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
