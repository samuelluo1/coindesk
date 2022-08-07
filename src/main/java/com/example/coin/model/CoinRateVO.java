package com.example.coin.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Samuel Luo
 */
@Data
public class CoinRateVO implements Serializable {

    private String time;

    private Set<CoinRate> rate;

    @Data
    @NoArgsConstructor
    public static class CoinRate implements Serializable {
        private String code;
        private String chinese;
        private Float rate;
    }
}
