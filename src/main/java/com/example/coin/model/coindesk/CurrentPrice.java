package com.example.coin.model.coindesk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Samuel Luo
 */
@Data
public class CurrentPrice implements Serializable {

    private Time time;

    private String disclaimer;

    private String chartName;

    private Map<String, Bpi> bpi;

    @Data
    @NoArgsConstructor
    public static class Time implements Serializable {
        private String updated;
        @JsonProperty("updatedISO")
        private String updatedIso;
        @JsonProperty("updateduk")
        private String updatedUk;
    }

    @Data
    @NoArgsConstructor
    public static class Bpi implements Serializable {
        private String code;
        private String symbol;
        private String rate;
        private String description;
        @JsonProperty("rate_float")
        private Float rateFloat;
    }
}
