package com.gregdm.javart.domain.art;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Greg on 27/03/2016.
 */
public class ArtImageUrlSize {

    @JsonProperty("width")
    private String width;

    @JsonProperty("height")
    private String height;

    @JsonProperty("url")
    private String url;


}
