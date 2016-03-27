package com.gregdm.javart.domain.art;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Greg on 26/03/2016.
 */
public class ArtImage {
    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("id")
    private long id;

    @JsonProperty("urls")
    private ArtImageUrl urls;

    @JsonProperty("photographer")
    private NameObject photographer;

    @JsonProperty("source")
    private NameObject source;
}
