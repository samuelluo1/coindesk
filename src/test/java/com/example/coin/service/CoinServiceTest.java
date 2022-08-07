package com.example.coin.service;

import com.example.coin.Application;
import com.example.coin.model.coindesk.CurrentPrice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Samuel Luo
 * Unit tests for {@link CoinService}.
 */
@SpringBootTest(classes = Application.class)
class CoinServiceTest {

    @Autowired
    private CoinService coinService;

    @Test
    void TestGetCurrentPrice() {
        CurrentPrice actual = coinService.getCurrentPrice();
        System.out.println(actual);
        Assertions.assertEquals(3, actual.getBpi().size());
    }
}