package com.example.coin.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * @author Samuel Luo
 */
@Data
@Entity
@Table(name = "COIN_CHINESE_CONVERSION")
public class CoinChineseConversion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "CODE", nullable = false)
    private String code;

    @NotNull
    @Column(name = "CHINESE", nullable = false)
    private String chinese;
}
