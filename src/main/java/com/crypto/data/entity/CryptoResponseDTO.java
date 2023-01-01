package com.crypto.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

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
}
