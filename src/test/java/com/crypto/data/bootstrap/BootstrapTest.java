package com.crypto.data.bootstrap;

import com.crypto.data.Bootstrap;
import com.crypto.data.entity.Crypto;
import com.crypto.data.repository.CryptoRepository;
import com.squareup.okhttp.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BootstrapTest {

    @Autowired
    private CryptoRepository cryptoRepository;

    @Autowired
    Bootstrap bootstrap;

    @DisplayName("JUnit test: Bootstrap.fetchResponse() and save in DB")
    @Test
    public void fetchResponseTest() throws IOException {
        List<String> currencies = List.of("BTC", "ETH", "XRP");

        for (String currency : currencies) {
            Response response = bootstrap.fetchResponse(currency, 24, 20);
            assertEquals(response.code(), HttpStatus.OK.value());
            assertTrue(cryptoRepository.existsByName(currency));

            Crypto crypto = cryptoRepository.findByName(currency);

            assertFalse(crypto.getLastPrice().isNaN());
            assertFalse(crypto.getId().isBlank());
            assertFalse(crypto.getName().isEmpty());
            assertTrue(crypto.getPrices().size() > 0);
        }
    }
}
