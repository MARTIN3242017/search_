package com.wk.search.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchEntity implements Serializable {
    private static final long serialVersionUID = 6431478674204338261L;
    private String id;
    private Float scope;
    private String keywordsList;
    private String blackWordsList;
    private String receiver;
}
