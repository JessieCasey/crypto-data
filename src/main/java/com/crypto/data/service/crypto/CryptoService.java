package com.crypto.data.service.crypto;

import com.crypto.data.entity.Crypto;
import com.crypto.data.entity.CryptoResponseDTO;

import java.util.List;
import java.util.Map;

/**
 * The CryptoService interface is required to create CryptoServiceImpl {@link CryptoServiceImpl}
 */

public interface CryptoService {
    Crypto findCryptoByName(String name);

    Map<String, Object> fetchCurrenciesWithPaginationAndSorting(String name, int pageNumber, int size, String sortOrder);

    CryptoResponseDTO fetchCryptoWithTheMinPrice(String name);

    CryptoResponseDTO fetchCryptoWithTheMaxPrice(String name);

    List<Crypto> findAll();

}
