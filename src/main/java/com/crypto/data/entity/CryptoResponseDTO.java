package com.crypto.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * CryptoResponseDTO is DTO that represents an object {@link Crypto}
 */

@Getter
@Setter
@Builder
public class CryptoResponseDTO {

    private String id;

    private String name;

    private Map<String, Crypto.Record> content;

    public static CryptoResponseDTO from(Crypto crypto, String content, Crypto.Record record) {
        return builder()
                .id(crypto.getId())
                .name(crypto.getName())
                .content(Map.of(content, record))
                .build();
    }

    @JsonIgnore
    public Float getRecordPrice() {
        return this.getContent().values().stream().findFirst().orElseThrow().getPrice();
    }
}
