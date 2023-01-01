package com.crypto.data.service.crypto;

import com.crypto.data.entity.Crypto;
import com.crypto.data.entity.CryptoResponseDTO;

import java.util.List;
import java.util.Map;

public interface CryptoService {
    Map<String, Object> fetchCryptosWithPaginationAndSorting(String name, int page, int size);

    CryptoResponseDTO fetchCryptoWithTheLowestPrice(String name);

    CryptoResponseDTO fetchCryptoWithTheMaxPrice(String name);

    List<Crypto> findAll();
}
