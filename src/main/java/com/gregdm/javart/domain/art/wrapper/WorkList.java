package com.gregdm.javart.domain.art.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gregdm.javart.domain.art.Work;

import java.util.List;

/**
 * Created by Greg on 26/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkList {

    @JsonProperty("subscriptions")
    private List<Work> works;

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }
}
