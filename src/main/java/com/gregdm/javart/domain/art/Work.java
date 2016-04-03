package com.gregdm.javart.domain.art;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Greg on 26/03/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Work {

    @JsonProperty("id")
    private String workId;
    @JsonProperty("popularity")
    private double popularity;
    @JsonProperty("title")
    private FrObject title;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("detail")
    private FrObject detail;
    @JsonProperty("date")
    private ArtDate date;
    @JsonProperty("inventory_number")
    private String inventoryNumber;
    @JsonProperty("use")
    private String use;
    @JsonProperty("height")
    private long height;
    @JsonProperty("width")
    private long width;
    @JsonProperty("depth")
    private Long depth;
    @JsonProperty("diameter")
    private String diameter;
    @JsonProperty("images")
    private List<ArtImage> images;
    @JsonProperty("wikipedia_url")
    private FrObject wikipediaUrl;
    @JsonProperty("wikipedia_extract")
    private FrObject wikipediaExtract;  
}
