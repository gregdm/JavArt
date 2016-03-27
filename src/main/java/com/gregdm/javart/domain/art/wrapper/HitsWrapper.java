package com.gregdm.javart.domain.art.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Greg on 26/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HitsWrapper {

    @JsonProperty("total")
    private long total;

    @JsonProperty("hits")
    private List<Hit> hits;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }
}
