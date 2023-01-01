package com.crypto.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("Cryptos")
@Getter
@Setter
@Builder
@ToString
public class Crypto {
    @Id
    private String id;

    private String name;

    @JsonProperty("last_price")
    private Float lastPrice;

    private List<Record> prices;

    @Builder
    @Getter
    @Setter
    public static class Record {
        private LocalDateTime time;
        private Float price;
    }
}
