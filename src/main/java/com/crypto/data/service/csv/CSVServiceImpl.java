package com.crypto.data.service.csv;

import com.crypto.data.entity.Crypto;
import com.crypto.data.exception.DataNotFoundException;
import com.crypto.data.service.crypto.CryptoService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * The CSVServiceImpl class implements CSVService interface to create methods to interact with CSV file {@link CSVService}
 */

@Service
public class CSVServiceImpl implements CSVService {

    private final String[] HEADERS = {"Count", "Cryptocurrency name", "Min Price", "Max Price"};
    private final CryptoService cryptoService;

    @Autowired
    public CSVServiceImpl(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    /**
     * Method creates a CSV file with the headers [Count, Cryptocurrency Name, Min Price and Max Price]
     */

    public ByteArrayInputStream generateCSVReport() {
        List<Crypto> cryptos = cryptoService.findAll();

        System.out.println(cryptos.size());
        if (cryptos.size() == 0)
            throw new DataNotFoundException("There is not enough data to create a CSV: The database is empty");

        final CSVFormat format = CSVFormat.DEFAULT.withHeader(HEADERS);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CSVPrinter printer = new CSVPrinter(new PrintWriter(out), format);

            int i = 0;
            for (Crypto crypto : cryptos) {
                Float max = cryptoService.fetchCryptoWithTheMaxPrice(crypto.getName()).getRecordPrice();
                Float min = cryptoService.fetchCryptoWithTheMinPrice(crypto.getName()).getRecordPrice();

                printer.printRecord(Arrays.asList(++i + "", crypto.getName(), min.toString(), max.toString()));
            }

            printer.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Fail to import data to CSV file: " + e.getMessage());
        }
    }

}
