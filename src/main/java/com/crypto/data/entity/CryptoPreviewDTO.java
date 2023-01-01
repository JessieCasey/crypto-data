package com.crypto.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CryptoPreviewDTO {

    private String id;

    private String name;

    @JsonProperty("last_price")
    private Float lastPrice;

    public static CryptoPreviewDTO from(Crypto crypto) {
        return builder()
                .id(crypto.getId())
                .name(crypto.getName())
                .lastPrice(crypto.getLastPrice())
                .build();
    }
}
