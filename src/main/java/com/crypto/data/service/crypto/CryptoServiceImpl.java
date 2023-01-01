package com.crypto.data.service.crypto;

import com.crypto.data.entity.Crypto;
import com.crypto.data.entity.CryptoPreviewDTO;
import com.crypto.data.entity.CryptoResponseDTO;
import com.crypto.data.repository.CryptoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CryptoServiceImpl implements CryptoService {

    private final CryptoRepository cryptoRepository;

    public CryptoServiceImpl(CryptoRepository cryptoRepository) {
        this.cryptoRepository = cryptoRepository;
    }

    @Override
    public Map<String, Object> fetchCryptosWithPaginationAndSorting(String name, int page, int size) {

        Pageable paging = PageRequest.of(page, size);

        Page<Crypto> pageTuts;
        if (name == null) pageTuts = cryptoRepository.findAll(paging);
        else pageTuts = cryptoRepository.findByNameContainingIgnoreCase(name, paging);

        List<CryptoPreviewDTO> cryptos = pageTuts.getContent().stream().map(CryptoPreviewDTO::from).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("content", cryptos);
        response.put("current_page", pageTuts.getNumber());
        response.put("total_items", pageTuts.getTotalElements());
        response.put("total_pages", pageTuts.getTotalPages());

        return response;
    }

    @Override
    public CryptoResponseDTO fetchCryptoWithTheLowestPrice(String name) {
        Crypto byName = cryptoRepository.findByName(name);
        Crypto.Record lowestPrice = byName.getPrices().stream().min(Comparator.comparing(Crypto.Record::getPrice)).get();
        return CryptoResponseDTO.from(byName, "lowest_price", lowestPrice);
    }

    @Override
    public CryptoResponseDTO fetchCryptoWithTheMaxPrice(String name) {
        Crypto byName = cryptoRepository.findByName(name);
        Crypto.Record lowestPrice = byName.getPrices().stream().max(Comparator.comparing(Crypto.Record::getPrice)).get();
        return CryptoResponseDTO.from(byName, "highest_price", lowestPrice);
    }

    @Override
    public List<Crypto> findAll() {
        return cryptoRepository.findAll();
    }
}
