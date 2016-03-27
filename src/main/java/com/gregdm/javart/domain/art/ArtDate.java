package com.gregdm.javart.domain.art;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Greg on 26/03/2016.
 */
public class ArtDate {
    @JsonProperty("display")
    private String display;

    @JsonProperty("estimated_day")
    private String estimatedDay;

    @JsonProperty("estimation_spread")
    private String estimationSpread;
}
