package com.crypto.data.controller;

import com.crypto.data.entity.Crypto;
import com.crypto.data.repository.CryptoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class CryptoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CryptoRepository cryptoRepository;

    private final String PATH = "/api/cryptocurrencies";

    @BeforeAll
    public void setUp() {
        Crypto crypto1 = Crypto.builder().id("63b352df3935c55dfb97e86e").name("DOGE")
                .lastPrice(0.83231f).prices(generateData(0.1f, 0.9f)).build();

        Crypto crypto2 = Crypto.builder().id("89b352df3235c55dib97e85f").name("SHIB")
                .lastPrice(0.0000865f).prices(generateData(0.000001f, 0.00009f)).build();

        Crypto crypto3 = Crypto.builder().id("31b353df3837c55dfb97e85l").name("SOL")
                .lastPrice(9.97f).prices(generateData(8f, 12.2f)).build();

        cryptoRepository.saveAll(List.of(crypto1, crypto2, crypto3));
    }

    @DisplayName("JUnit test: fetch Crypto With The Lowest Price")
    @Test
    public void fetchCryptoWithTheLowestPrice() throws Exception {

        this.mockMvc.perform(get(PATH + "/minprice?name=DOGE"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXPECTED_MIN[0])));

        this.mockMvc.perform(get(PATH + "/minprice?name=SOL"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXPECTED_MIN[1])));

        this.mockMvc.perform(get(PATH + "/minprice?name=SHIB"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXPECTED_MIN[2])));
    }

    @DisplayName("JUnit test: fetch Crypto With The Highest Price")
    @Test
    public void fetchCryptoWithTheMaxPrice() throws Exception {

        this.mockMvc.perform(get(PATH + "/maxprice?name=DOGE"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXPECTED_MAX[0])));

        this.mockMvc.perform(get(PATH + "/maxprice?name=SOL"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXPECTED_MAX[1])));

        this.mockMvc.perform(get(PATH + "/maxprice?name=SHIB"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXPECTED_MAX[2])));
    }

    @DisplayName("JUnit test: fetch Crypto With The Highest and lowest Price with not exist name")
    @Test
    public void fetchCryptoWithTheMaxAndMaxPriceWithNotExist() throws Exception {

        String notExistedName = "12fkasd";
        this.mockMvc.perform(get(PATH + "/maxprice?name=" + notExistedName))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"message\":\"Crypto with the name '" + notExistedName + "' is not found\"")));

        this.mockMvc.perform(get(PATH + "/minprice?name=" + notExistedName))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"message\":\"Crypto with the name '" + notExistedName + "' is not found\"")));
    }

    @DisplayName("JUnit test: fetch Crypto With The Highest and lowest Price with empty name")
    @Test
    public void fetchCryptoWithTheMaxAndMaxPriceWithEmpthy() throws Exception {

        String notExistedName = " ";
        this.mockMvc.perform(get(PATH + "/maxprice?name=" + notExistedName))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(get(PATH + "/minprice?name=" + notExistedName))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("JUnit test: fetch Currencies With Pagination And Sorting")
    @Test
    public void fetchCurrenciesWithPaginationAndSorting() throws Exception {

        this.mockMvc.perform(get(PATH + "?name=S&page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXPECTED_PAGE)));

    }

    @DisplayName("JUnit test: generate CSV Report")
    @Test
    public void generateCSVReport() throws Exception {

        this.mockMvc.perform(get(PATH + "/csv"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/csv"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=report_" + LocalDate.now() + ".csv"));

    }

    public List<Crypto.Record> generateData(float min, float max) {
        List<Crypto.Record> record = new ArrayList<>();
        LocalDateTime of = LocalDateTime.of(1900, 1, 1, 1, 1);
        record.add(Crypto.Record.builder().price(min).time(of).build());
        record.add(Crypto.Record.builder().price(max).time(of).build());
        for (int i = 0; i < 10; i++) {
            record.add(Crypto.Record.builder()
                    .price(ThreadLocalRandom.current().nextFloat(min, max))
                    .time(of)
                    .build());
        }
        return record;
    }

    @AfterAll
    public void deleteAll() {
        cryptoRepository.deleteByName("DOGE");
        cryptoRepository.deleteByName("SHIB");
        cryptoRepository.deleteByName("SOL");
    }

    String[] EXPECTED_MIN = {
            "{\"id\":\"63b352df3935c55dfb97e86e\",\"name\":\"DOGE\",\"content\":" +
                    "{\"lowest_price\":{\"time\":\"1900-01-01T01:01:00\",\"price\":0.1}}}",

            "{\"id\":\"31b353df3837c55dfb97e85l\",\"name\":\"SOL\",\"content\":" +
                    "{\"lowest_price\":{\"time\":\"1900-01-01T01:01:00\",\"price\":8.0}}}",

            "{\"id\":\"89b352df3235c55dib97e85f\",\"name\":\"SHIB\",\"content\":" +
                    "{\"lowest_price\":{\"time\":\"1900-01-01T01:01:00\",\"price\":1.0E-6}}}"};

    String[] EXPECTED_MAX = {
            "{\"id\":\"63b352df3935c55dfb97e86e\",\"name\":\"DOGE\",\"content\":" +
                    "{\"highest_price\":{\"time\":\"1900-01-01T01:01:00\",\"price\":0.9}}}",

            "{\"id\":\"31b353df3837c55dfb97e85l\",\"name\":\"SOL\",\"content\":" +
                    "{\"highest_price\":{\"time\":\"1900-01-01T01:01:00\",\"price\":12.2}}}",

            "{\"id\":\"89b352df3235c55dib97e85f\",\"name\":\"SHIB\",\"content\":" +
                    "{\"highest_price\":{\"time\":\"1900-01-01T01:01:00\",\"price\":9.0E-5}}}"};

    String EXPECTED_PAGE = "{\"total_pages\":1,\"total_items\":2," +
            "\"content\":[" +
                "{\"id\":\"89b352df3235c55dib97e85f\",\"name\":\"SHIB\",\"last_price\":8.65E-5}," +
                "{\"id\":\"31b353df3837c55dfb97e85l\",\"name\":\"SOL\",\"last_price\":9.97}]," +
            "\"current_page\":0}";
}
