package com.gregdm.javart.domain.art.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gregdm.javart.domain.art.Work;

import java.util.List;

/**
 * Created by Greg on 27/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseWrapper {

    @JsonProperty("took")
    private int took;

    @JsonProperty("timed_out")
    private boolean timeOut;

    @JsonProperty("hits")
    private HitsWrapper hits;

    public int getTook() {
        return took;
    }

    public void setTook(int took) {
        this.took = took;
    }

    public boolean isTimeOut() {
        return timeOut;
    }

    public void setTimeOut(boolean timeOut) {
        this.timeOut = timeOut;
    }

    public HitsWrapper getHits() {
        return hits;
    }

    public void setHits(HitsWrapper hits) {
        this.hits = hits;
    }
}
