package com.tinybank.main.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.net.URL;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record Links(String nextPage,
                    String prevPage,
                    String self) {
}
