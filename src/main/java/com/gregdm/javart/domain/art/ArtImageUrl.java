package com.gregdm.javart.domain.art;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Greg on 27/03/2016.
 */
public class ArtImageUrl {

    @JsonProperty("original")
    private String original;

    @JsonProperty("small")
    private ArtImageUrlSize small;
    @JsonProperty("thumbnail")
    private ArtImageUrlSize thumbnail;
    @JsonProperty("medium")
    private ArtImageUrlSize medium;
    @JsonProperty("large")
    private ArtImageUrlSize large;


}
