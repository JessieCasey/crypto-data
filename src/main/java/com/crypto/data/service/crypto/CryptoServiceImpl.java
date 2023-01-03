package com.crypto.data.service.crypto;

import com.crypto.data.entity.Crypto;
import com.crypto.data.entity.CryptoPreviewDTO;
import com.crypto.data.entity.CryptoResponseDTO;
import com.crypto.data.exception.EntityNotFoundException;
import com.crypto.data.repository.CryptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The CryptoServiceImpl class implements CryptoService interface to create methods to interact with Crypto entity {@link CryptoService}
 */

@Service
public class CryptoServiceImpl implements CryptoService {

    private final CryptoRepository cryptoRepository;

    @Autowired
    public CryptoServiceImpl(CryptoRepository cryptoRepository) {
        this.cryptoRepository = cryptoRepository;
    }

    @Override
    public Map<String, Object> fetchCurrenciesWithPaginationAndSorting(String name, int pageNumber, int size, String sortOrder) {
        if (pageNumber < 0) throw new IllegalArgumentException("'pageNumber' cannot be less than zero");
        if (size <= 0) throw new IllegalArgumentException("'size' should be greater than zero");

        Pageable paging = PageRequest.of(pageNumber, size);

        Page<Crypto> page;
        if (name == null || !(name.trim().length() > 0)) page = cryptoRepository.findAll(paging);
        else page = cryptoRepository.findByNameContainingIgnoreCase(name, paging);

        List<CryptoPreviewDTO> cryptos;
        if (sortOrder == null || sortOrder.equals("ASC")) {
            cryptos = page.getContent().stream()
                    .sorted(Comparator.comparing(Crypto::getLastPrice))
                    .map(CryptoPreviewDTO::from).toList();
        } else {
            cryptos = page.getContent().stream()
                    .sorted(Comparator.comparing(Crypto::getLastPrice).reversed())
                    .map(CryptoPreviewDTO::from).toList();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", cryptos);
        response.put("current_page", page.getNumber());
        response.put("total_items", page.getTotalElements());
        response.put("total_pages", page.getTotalPages());

        return response;
    }

    @Override
    public CryptoResponseDTO fetchCryptoWithTheMinPrice(String name) {
        Crypto byName = findCryptoByName(name);
        Crypto.Record lowestPrice = byName.getPrices().stream().min(Comparator.comparing(Crypto.Record::getPrice)).get();
        return CryptoResponseDTO.from(byName, "lowest_price", lowestPrice);
    }

    @Override
    public CryptoResponseDTO fetchCryptoWithTheMaxPrice(String name) {
        Crypto byName = findCryptoByName(name);
        Crypto.Record lowestPrice = byName.getPrices().stream().max(Comparator.comparing(Crypto.Record::getPrice)).get();
        return CryptoResponseDTO.from(byName, "highest_price", lowestPrice);
    }

    @Override
    public Crypto findCryptoByName(String name) {
        if (cryptoRepository.existsByName(name)) {
            return cryptoRepository.findByName(name);
        } else {
            throw new EntityNotFoundException("Crypto with the name '" + name + "' is not found");
        }
    }

    @Override
    public List<Crypto> findAll() {
        return cryptoRepository.findAll();
    }
}
