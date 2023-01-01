package com.crypto.data.controller;

import com.crypto.data.exception.MessageResponse;
import com.crypto.data.service.crypto.CryptoService;
import com.crypto.data.service.csv.CSVService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/cryptocurrencies")
@Slf4j
public class CryptoController {
    private final CryptoService cryptoService;
    private final CSVService csvService;

    /**
     * Constructor for {@link CryptoController}.
     *
     * @param cryptoService {@link CryptoService}
     * @param csvService    {@link CSVService}
     */

    public CryptoController(CryptoService cryptoService, CSVService csvService) {
        this.cryptoService = cryptoService;
        this.csvService = csvService;
    }

    @GetMapping
    public ResponseEntity<?> fetchCryptosWithPaginationAndSorting(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ASC") String sortOrder,
            WebRequest request) {
        log.info("[GET] Request to resource '/api/cryptocurrencies', method 'fetchCryptosWithPaginationAndSorting'");
        try {
            return ResponseEntity.ok(cryptoService.fetchCryptosWithPaginationAndSorting(name, page, size, sortOrder));
        } catch (Exception e) {
            log.error("[GET] Error in method 'fetchCryptosWithPaginationAndSorting': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), request));
        }
    }

    @GetMapping("/minprice")
    public ResponseEntity<?> fetchCryptoWithTheLowestPrice(@RequestParam String name, WebRequest request) {
        log.info("[GET] Request to resource '/api/cryptocurrencies/minprice', method 'fetchCryptoWithTheLowestPrice'");
        if (!name.isEmpty()) {
            return ResponseEntity.ok(cryptoService.fetchCryptoWithTheMinPrice(name));
        } else {
            log.warn("[GET] Warning in method 'fetchCryptoWithTheLowestPrice'");
            return ResponseEntity.badRequest().body(new MessageResponse("You need to provide cryptocurrency name in order to get the lowest price", request));
        }
    }

    @GetMapping("/maxprice")
    public ResponseEntity<?> fetchCryptoWithTheMaxPrice(@RequestParam String name, WebRequest request) {
        log.info("[GET] Request to resource '/api/cryptocurrencies/maxprice', method 'fetchCryptoWithTheMaxPrice'");
        if (!name.isEmpty()) {
            return ResponseEntity.ok(cryptoService.fetchCryptoWithTheMaxPrice(name));
        } else {
            log.warn("[GET] Warning in method 'fetchCryptoWithTheMaxPrice'");
            return ResponseEntity.badRequest().body(new MessageResponse("You need to provide cryptocurrency name in order to get the highest price", request));
        }
    }

    @GetMapping("/csv")
    public ResponseEntity<MessageResponse> generateCSVReport(WebRequest request) {
        log.info("[GET] Request to resource '/api/cryptocurrencies/csv', method 'generateCSVReport'");
        try {
            csvService.generateCSVReport();
            return ResponseEntity.ok().body(new MessageResponse(200, "CSV with a name 'report_" + LocalDate.now() + ".csv' is successfully created", request));
        } catch (IOException e) {
            log.error("[GET] Error in method 'generateCSVReport': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), request));
        }
    }
}
