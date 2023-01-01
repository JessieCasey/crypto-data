package com.crypto.data.controller;

import com.crypto.data.service.crypto.CryptoService;
import com.crypto.data.service.csv.CSVService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/cryptocurrencies")
@Slf4j
public class CryptoController {
    private final CryptoService cryptoService;
    private final CSVService csvService;

    public CryptoController(CryptoService cryptoService, CSVService csvService) {
        this.cryptoService = cryptoService;
        this.csvService = csvService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> fetchCryptosWithPaginationAndSorting(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return new ResponseEntity<>(
                    cryptoService.fetchCryptosWithPaginationAndSorting(name, page, size),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/minprice")
    public ResponseEntity<?> fetchCryptoWithTheLowestPrice(@RequestParam(required = true) String name) {
        try {
            return new ResponseEntity<>(cryptoService.fetchCryptoWithTheLowestPrice(name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/maxprice")
    public ResponseEntity<?> fetchCryptoWithTheMaxPrice(@RequestParam(required = true) String name) {
        try {
            return new ResponseEntity<>(cryptoService.fetchCryptoWithTheMaxPrice(name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/csv")
    public void generateCSVReport() {
        try {
            csvService.generateCSVReport();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
