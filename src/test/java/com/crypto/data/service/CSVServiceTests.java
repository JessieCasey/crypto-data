package com.crypto.data.service;

import com.crypto.data.DataApplication;
import com.crypto.data.entity.Crypto;
import com.crypto.data.repository.CryptoRepository;
import com.crypto.data.service.csv.CSVService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = DataApplication.class)
@Slf4j
class CSVServiceTests {

    @Autowired
    private CSVService csvService;

    @Autowired
    private CryptoRepository cryptoRepository;

    @BeforeAll
    public void setUp() {
        Crypto crypto1 = Crypto.builder().name("DOGE").lastPrice(0.83231f).prices(generateData(0.1f, 0.9f)).build();
        Crypto crypto2 = Crypto.builder().name("SHIB").lastPrice(0.0000865f).prices(generateData(0.000001f, 0.00009f)).build();
        Crypto crypto3 = Crypto.builder().name("SOL").lastPrice(9.97f).prices(generateData(8f, 12.2f)).build();

        cryptoRepository.saveAll(List.of(crypto1, crypto2, crypto3));
    }

    @AfterAll
    public void deleteAll() {
        cryptoRepository.deleteByName("DOGE");
        cryptoRepository.deleteByName("SHIB");
        cryptoRepository.deleteByName("SOL");
    }

    @DisplayName("JUnit test: generate CSV Report method")
    @Test
    public void generateCSVReport() {
        ByteArrayInputStream byteArrayInputStream = csvService.generateCSVReport();
        int n = byteArrayInputStream.available();
        byte[] bytes = new byte[n];
        byteArrayInputStream.read(bytes, 0, n);
        String csvToString = new String(bytes, StandardCharsets.UTF_8);

        assertFalse(csvToString.isEmpty());
        assertTrue(csvToString.contains("Cryptocurrency name,Min Price,Max Price"));
        assertTrue(csvToString.contains(",DOGE,0.1,0.9"));
        assertTrue(csvToString.contains(",SOL,8.0,12.2"));
        assertTrue(csvToString.contains(",SHIB,1.0E-6,9.0E-5"));
    }

    public List<Crypto.Record> generateData(float min, float max) {
        List<Crypto.Record> record = new ArrayList<>();
        record.add(Crypto.Record.builder().price(min).time(LocalDateTime.now()).build());
        record.add(Crypto.Record.builder().price(max).time(LocalDateTime.now()).build());
        for (int i = 0; i < 10; i++) {
            record.add(Crypto.Record.builder()
                    .price(ThreadLocalRandom.current().nextFloat(min, max))
                    .time(LocalDateTime.now())
                    .build());
        }
        return record;
    }

}
