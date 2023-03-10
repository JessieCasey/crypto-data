package com.crypto.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The Crypto entity
 */

@Document("Currencies")
@Getter
@Setter
@Builder
@ToString
public class Crypto {

    @Id
    private String id;

    private String name;

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
