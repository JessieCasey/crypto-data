package com.crypto.data.service.crypto;

import com.crypto.data.entity.Crypto;
import com.crypto.data.entity.CryptoResponseDTO;

import java.util.List;
import java.util.Map;

public interface CryptoService {

    Crypto findCryptoByName(String name);

    Map<String, Object> fetchCryptosWithPaginationAndSorting(String name, int pageNumber, int size, String sortOrder);

    CryptoResponseDTO fetchCryptoWithTheMinPrice(String name);

    CryptoResponseDTO fetchCryptoWithTheMaxPrice(String name);

    List<Crypto> findAll();

}
