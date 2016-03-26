package com.gregdm.javart.repository.art;

import com.gregdm.javart.domain.art.Work;
import com.gregdm.javart.domain.art.wrapper.WorkList;
import com.gregdm.javart.service.art.config.ArtApiProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;

/**
 * Created by Greg on 26/03/2016.
 */
@EnableConfigurationProperties(ArtApiProperties.class)
public class WorkRepository {

    @Inject
    private ArtApiProperties properties;

    @Inject
    protected HttpHeaders artApiHeader;

    @Inject
    protected RestTemplate artApiRestTemplate;

    public Work get(String id) {
        try {
            String url = properties.getUrl() + "works/" + id;

            HttpEntity<String> entity = new HttpEntity<>(artApiHeader);
            ResponseEntity<WorkList> responseEntity = artApiRestTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                WorkList.class
            );
            return responseEntity.getBody().getWorks().get(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
