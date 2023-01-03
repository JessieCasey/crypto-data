package com.crypto.data.service;

import com.crypto.data.DataApplication;
import com.crypto.data.entity.Crypto;
import com.crypto.data.entity.CryptoPreviewDTO;
import com.crypto.data.entity.CryptoResponseDTO;
import com.crypto.data.exception.EntityNotFoundException;
import com.crypto.data.repository.CryptoRepository;
import com.crypto.data.service.crypto.CryptoService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = DataApplication.class)
class CryptoServiceTests {

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private CryptoRepository cryptoRepository;

    @BeforeAll
    public void setUp() {
        Crypto crypto1 = Crypto.builder().name("DOGE")
                .lastPrice(0.83231f).prices(generateData(0.1f, 0.9f)).build();

        Crypto crypto2 = Crypto.builder().name("SHIB")
                .lastPrice(0.0000865f).prices(generateData(0.000001f, 0.00009f)).build();

        Crypto crypto3 = Crypto.builder().name("SOL")
                .lastPrice(9.97f).prices(generateData(8f, 12.2f)).build();

        cryptoRepository.saveAll(List.of(crypto1, crypto2, crypto3));
    }

    @DisplayName("JUnit test: fetch Currencies With Pagination And Sorting method")
    @Test
    public void fetchCurrenciesWithPaginationAndSorting() {
        Map<String, Object> actual = cryptoService.fetchCurrenciesWithPaginationAndSorting("S", 0, 3, null);

        List<CryptoPreviewDTO> content = (List<CryptoPreviewDTO>) actual.get("content");
        int current_page = (int) actual.get("current_page");

        assertEquals(content.get(0).getName(), "SHIB");
        assertEquals(content.get(content.size() - 1).getName(), "SOL");
        assertEquals(current_page, 0);
    }

    @DisplayName("JUnit test: fetch Currencies With Pagination And Sorting ASC method")
    @Test
    public void fetchCurrenciesWithPaginationAndSortingASC() {
        Map<String, Object> actual = cryptoService.fetchCurrenciesWithPaginationAndSorting("S", 1, 1, "ASC");

        List<CryptoPreviewDTO> content = (List<CryptoPreviewDTO>) actual.get("content");
        int current_page = (int) actual.get("current_page");

        assertEquals(content.get(0).getName(), "SOL");
        assertEquals(current_page, 1);
    }

    @DisplayName("JUnit test: fetch Currencies With Wrong Arguments method")
    @Test
    public void fetchCurrenciesWithWrongArguments() {
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class,
                () -> cryptoService.fetchCurrenciesWithPaginationAndSorting("S", -1, 1, "ASC"));

        assertEquals("'pageNumber' cannot be less than zero", t.getMessage());

        Throwable t2 = Assertions.assertThrows(IllegalArgumentException.class,
                () -> cryptoService.fetchCurrenciesWithPaginationAndSorting("S", 0, -1, "ASC"));

        assertEquals("'size' should be greater than zero", t2.getMessage());
    }

    @DisplayName("JUnit test: fetch Crypto With The Min Price method")
    @Test
    public void fetchCryptoWithTheMinPrice() {
        CryptoResponseDTO sol = cryptoService.fetchCryptoWithTheMinPrice("SOL");

        assertEquals(sol.getName(), "SOL");
        assertEquals(sol.getRecordPrice(), 8f, 0.0);

        CryptoResponseDTO doge = cryptoService.fetchCryptoWithTheMinPrice("DOGE");

        assertEquals(doge.getName(), "DOGE");
        assertEquals(doge.getRecordPrice(), 0.1f, 0.0);

        CryptoResponseDTO shib = cryptoService.fetchCryptoWithTheMinPrice("SHIB");

        assertEquals(shib.getName(), "SHIB");
        assertEquals(shib.getRecordPrice(), 0.000001f, 0.0);
    }

    @DisplayName("JUnit test: fetch Crypto With The Max Price method")
    @Test
    public void fetchCryptoWithTheMaxPrice() {
        CryptoResponseDTO sol = cryptoService.fetchCryptoWithTheMaxPrice("SOL");

        assertEquals(sol.getName(), "SOL");
        assertEquals(sol.getRecordPrice(), 12.2f, 0.0);

        CryptoResponseDTO doge = cryptoService.fetchCryptoWithTheMaxPrice("DOGE");

        assertEquals(doge.getName(), "DOGE");
        assertEquals(doge.getRecordPrice(), 0.9f, 0.0);

        CryptoResponseDTO shib = cryptoService.fetchCryptoWithTheMaxPrice("SHIB");

        assertEquals(shib.getName(), "SHIB");
        assertEquals(shib.getRecordPrice(), 0.00009f, 0.0);
    }

    @DisplayName("JUnit test: find Crypto By Name method")
    @Test
    public void findCryptoByName() {
        Crypto crypto = cryptoService.findCryptoByName("DOGE");
        assertEquals(crypto.getName(), "DOGE");
        Crypto crypto2 = cryptoService.findCryptoByName("SHIB");
        assertEquals(crypto2.getName(), "SHIB");
        Crypto crypto3 = cryptoService.findCryptoByName("SOL");
        assertEquals(crypto3.getName(), "SOL");
    }

    @DisplayName("JUnit test: find Crypto By Not Existed Name method")
    @Test
    public void findCryptoByNotExistedName() {
        String name = "KOLD";
        Throwable t = Assertions.assertThrows(EntityNotFoundException.class,
                () -> cryptoService.findCryptoByName(name));

        assertEquals("Crypto with the name '" + name + "' is not found", t.getMessage());

        Throwable t2 = Assertions.assertThrows(EntityNotFoundException.class,
                () -> cryptoService.findCryptoByName(null));

        assertEquals("Crypto with the name '" + "null" + "' is not found", t2.getMessage());

        Throwable t3 = Assertions.assertThrows(EntityNotFoundException.class,
                () -> cryptoService.findCryptoByName("#$_5414"));

        assertEquals("Crypto with the name '" + "#$_5414" + "' is not found", t3.getMessage());
    }

    @DisplayName("JUnit test: find All method")
    @Test
    public void findAll() {
        List<Crypto> all = cryptoService.findAll();
        Assertions.assertFalse(all.isEmpty());
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

    @AfterAll
    public void deleteAll() {
        cryptoRepository.deleteByName("DOGE");
        cryptoRepository.deleteByName("SHIB");
        cryptoRepository.deleteByName("SOL");
    }

}
