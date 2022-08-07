package com.example.coin.web.rest;

import com.example.coin.Application;
import com.example.coin.model.CoinChineseConversion;
import com.example.coin.repository.CoinChineseConversionRepository;
import com.example.coin.service.CoinService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author Samuel Luo
 * Integration tests for the {@link CoinController} REST controller.
 */
@SpringBootTest(classes = Application.class)
class CoinControllerTest {

    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_CODE = "USD";
    private static final String DEFAULT_CHINESE = "美金";

    @Autowired
    private CoinService coinService;

    @Autowired
    private CoinChineseConversionRepository coinChineseConversionRepository;

    private MockMvc restUserMockMvc;

    private static final ObjectMapper mapper = createObjectMapper();

    private CoinChineseConversion coinChineseConversion;

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @BeforeEach
    public void setup() {
        CoinController coinConversionController = new CoinController(coinService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(coinConversionController).build();
    }

    @BeforeEach
    public void initTest() {
        CoinChineseConversion coinChineseConversion = new CoinChineseConversion();
        coinChineseConversion.setId(DEFAULT_ID);
        coinChineseConversion.setCode(DEFAULT_CODE);
        coinChineseConversion.setChinese(DEFAULT_CHINESE);
        this.coinChineseConversion = coinChineseConversion;
    }

    @Test
    @Transactional
    void getChinese() throws Exception {
        restUserMockMvc.perform(get("/api/v1/coin/conversion/chinese/{code}", coinChineseConversion.getCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DEFAULT_ID))
                .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
                .andExpect(jsonPath("$.chinese").value(DEFAULT_CHINESE))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void getNonExistingChinese() throws Exception {
        restUserMockMvc.perform(get("/api/v1/coin/conversion/chinese/unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void createChinese() throws Exception {
        int databaseSizeBeforeCreate = coinChineseConversionRepository.findAll().size();

        // Create the CoinChineseConversion
        CoinChineseConversion coinChineseConversion = new CoinChineseConversion();
        coinChineseConversion.setCode("newCode");
        coinChineseConversion.setChinese("newChinese");

        restUserMockMvc.perform(post("/api/v1/coin/conversion/chinese")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsBytes(coinChineseConversion)))
                .andExpect(status().isOk());

        // Validate the CoinChineseConversion in the database
        List<CoinChineseConversion> coinChineseConversionList = coinChineseConversionRepository.findAll();
        assertThat(coinChineseConversionList).hasSize(databaseSizeBeforeCreate + 1);
        CoinChineseConversion testCoinChineseConversion = coinChineseConversionList.get(coinChineseConversionList.size() - 1);
        assertThat(testCoinChineseConversion.getId()).isEqualTo(4L);
        assertThat(testCoinChineseConversion.getCode()).isEqualTo("newCode");
        assertThat(testCoinChineseConversion.getChinese()).isEqualTo("newChinese");
    }

    @Test
    @Transactional
    void updateChinese() throws Exception {
        // Initialize the database
        int databaseSizeBeforeUpdate = coinChineseConversionRepository.findAll().size();

        // Update the user
        CoinChineseConversion updatedCoinChineseConversion = coinChineseConversionRepository.findById(coinChineseConversion.getId()).get();

        CoinChineseConversion coinChineseConversion = new CoinChineseConversion();
        coinChineseConversion.setId(updatedCoinChineseConversion.getId());
        coinChineseConversion.setCode("updatedCode");
        coinChineseConversion.setChinese("updatedChinese");

        restUserMockMvc.perform(put("/api/v1/coin/conversion/chinese")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(mapper.writeValueAsBytes(coinChineseConversion)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // Validate the User in the database
        List<CoinChineseConversion> coinChineseConversionList = coinChineseConversionRepository.findAll();
        assertThat(coinChineseConversionList).hasSize(databaseSizeBeforeUpdate);
        CoinChineseConversion testCoinChineseConversion = coinChineseConversionList.get(0);
        assertThat(testCoinChineseConversion.getCode()).isEqualTo("updatedCode");
        assertThat(testCoinChineseConversion.getChinese()).isEqualTo("updatedChinese");
    }

    @Test
    @Transactional
    void deleteChinese() throws Exception {
        // Initialize the database
        int databaseSizeBeforeDelete = coinChineseConversionRepository.findAll().size();

        // Delete the user
        restUserMockMvc.perform(delete("/api/v1/coin/conversion/chinese/{id}", coinChineseConversion.getId()))
                .andExpect(status().isNoContent());

        // Validate the database is empty
        List<CoinChineseConversion> coinChineseConversionList = coinChineseConversionRepository.findAll();
        assertThat(coinChineseConversionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    void getCoinRate() throws Exception {
        restUserMockMvc.perform(get("/api/v1/coin/rate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate[*].code").value(hasItem("USD")))
                .andExpect(jsonPath("$.rate[*].chinese").value(hasItem("美金")))
                .andExpect(jsonPath("$.rate[*].code").value(hasItem("EUR")))
                .andExpect(jsonPath("$.rate[*].chinese").value(hasItem("歐元")))
                .andExpect(jsonPath("$.rate[*].code").value(hasItem("GBP")))
                .andExpect(jsonPath("$.rate[*].chinese").value(hasItem("英鎊")))
                .andDo(MockMvcResultHandlers.print());
    }
}