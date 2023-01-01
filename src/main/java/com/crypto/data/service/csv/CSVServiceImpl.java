package com.crypto.data.service.csv;

import com.crypto.data.entity.Crypto;
import com.crypto.data.service.crypto.CryptoService;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService {

    private final CryptoService cryptoService;

    public CSVServiceImpl(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @Override
    public void generateCSVReport() throws IOException {
        List<String[]> csvData = createCsvDataSimple();

        try (CSVWriter writer = new CSVWriter(new FileWriter("./reports/report_" + LocalDate.now() + ".csv"))) {
            writer.writeAll(csvData);
        }
    }


    private List<String[]> createCsvDataSimple() {
        List<Crypto> cryptos = cryptoService.findAll();

        List<String[]> content = new ArrayList<>();
        content.add(new String[]{"Count", "Cryptocurrency name", "Min Price", "Max Price"});

        for (int i = 0; i < cryptos.size(); ) {
            Crypto crypto = cryptos.get(i);
            Float maxPrice = cryptoService.fetchCryptoWithTheMaxPrice(crypto.getName()).getRecordPrice();
            Float minPrice = cryptoService.fetchCryptoWithTheMinPrice(crypto.getName()).getRecordPrice();

            content.add(new String[]{++i + "", crypto.getName(), minPrice.toString(), maxPrice.toString()});
        }

        return content;
    }
}
