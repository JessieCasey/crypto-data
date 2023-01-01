package com.crypto.data;

import com.crypto.data.entity.Crypto;
import com.crypto.data.repository.CryptoRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Bootstrap class initiates DB
 *
 * @author Artem Komarov
 */

@Component
@Slf4j
public class Bootstrap {

    @Bean
    CommandLineRunner runner(CryptoRepository cryptoRepository) {
        cryptoRepository.deleteAll();
        log.info("Class 'Bootstrap' method 'runner()': Database is erased");

        return args -> {
            List<String> currencies = List.of("BTC", "ETH", "XRP");
            int lastHours = 24;
            int maxResponseSize = 20;

            for (String currency : currencies) {
                Response response = fetchResponse(currency, lastHours, maxResponseSize);
                log.info("Class 'Bootstrap' method 'runner()': The response was received from a third-party application");
                List<Wrapper> wrappers = new Gson().fromJson(response.body().string(),
                        new TypeToken<List<Wrapper>>() {}.getType());

                List<Crypto.Record> records = new ArrayList<>();

                wrappers.forEach(x -> records.add(Crypto.Record.builder()
                        .time(LocalDateTime.ofInstant(Instant.ofEpochSecond(x.tmsp), TimeZone.getTimeZone("Europe/Kiev").toZoneId()))
                        .price(x.price)
                        .build()));

                cryptoRepository.save(Crypto.builder()
                        .name(currency)
                        .prices(records)
                        .lastPrice(records.get(records.size() - 1).getPrice())
                        .build());
            }
            log.info("Class 'Bootstrap' method 'runner()': Cryptocurrencies are saved in DB successfully");
        };
    }

    public Response fetchResponse(String currency, int lastHours, int maxResponseSize) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\"lastHours\": " + lastHours + "," +
                        "\"maxRespArrSize\": " + maxResponseSize + "}");

        Request request = new Request.Builder()
                .url("https://cex.io/api/price_stats/" + currency + "/USD")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        return client.newCall(request).execute();
    }

    static class Wrapper {
        long tmsp;
        Float price;
    }
}
