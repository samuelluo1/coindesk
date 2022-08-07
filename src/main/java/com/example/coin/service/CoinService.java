package com.example.coin.service;

import com.example.coin.config.JavaScriptMessageConverter;
import com.example.coin.model.CoinChineseConversion;
import com.example.coin.model.CoinRateVO;
import com.example.coin.model.coindesk.CurrentPrice;
import com.example.coin.repository.CoinChineseConversionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Samuel Luo
 */
@Service
public class CoinService {

    private static final String URI_CURRENT_PRICE = "https://api.coindesk.com/v1/bpi/currentprice.json";

    private final CoinChineseConversionRepository coinChineseConversionRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public CoinService(CoinChineseConversionRepository coinChineseConversionRepository) {
        this.coinChineseConversionRepository = coinChineseConversionRepository;
    }

    @Transactional(readOnly = true)
    public Optional<CoinChineseConversion> findByCode(String code) {
        return coinChineseConversionRepository.findByCode(code);
    }

    public CoinChineseConversion save(CoinChineseConversion coinChineseConversion) {
        return coinChineseConversionRepository.save(coinChineseConversion);
    }

    public void delete(Long id) {
        coinChineseConversionRepository.deleteById(id);
    }

    public CurrentPrice getCurrentPrice() {
        restTemplate.getMessageConverters().add(new JavaScriptMessageConverter());
        return restTemplate.getForObject(URI_CURRENT_PRICE, CurrentPrice.class);
    }

    public CoinRateVO getCoinRate() {
        CurrentPrice currentPrice = getCurrentPrice();
        return currentPriceToCoinRateVo(Objects.requireNonNull(currentPrice));
    }

    private CoinRateVO currentPriceToCoinRateVo(CurrentPrice currentPrice) {
        CoinRateVO coinRateVO = new CoinRateVO();

        String time = currentPrice.getTime().getUpdatedIso();
        time = LocalDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        coinRateVO.setTime(time);

        coinRateVO.setRate(currentPrice.getBpi().values().stream().map(bpi ->
                coinChineseConversionRepository.findByCode(bpi.getCode())
                        .map(coinChineseConversion -> {
                            CoinRateVO.CoinRate coinRate = new CoinRateVO.CoinRate();
                            coinRate.setCode(bpi.getCode());
                            coinRate.setChinese(coinChineseConversion.getChinese());
                            coinRate.setRate(bpi.getRateFloat());
                            return coinRate;
                        })
                        .orElseThrow(RuntimeException::new))
                .collect(Collectors.toSet())
        );

        return coinRateVO;
    }
}
