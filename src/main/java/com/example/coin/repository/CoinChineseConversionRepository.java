package com.example.coin.repository;

import com.example.coin.model.CoinChineseConversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author Samuel Luo
 */
public interface CoinChineseConversionRepository extends
        JpaRepository<CoinChineseConversion, Long>, JpaSpecificationExecutor<CoinChineseConversion> {
    Optional<CoinChineseConversion> findByCode(@Param("code") String code);
}
