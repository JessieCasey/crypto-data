package com.crypto.data.controller;

import com.crypto.data.exception.MessageResponse;
import com.crypto.data.service.crypto.CryptoService;
import com.crypto.data.service.csv.CSVService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;

@RestController
@RequestMapping("api/cryptocurrencies")
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
    public ResponseEntity<?> fetchCurrenciesWithPaginationAndSorting(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ASC") String sortOrder,
            WebRequest request) {
        log.info("[GET] Request to resource '/api/cryptocurrencies', method 'fetchCurrenciesWithPaginationAndSorting'");
        try {
            return ResponseEntity.ok(cryptoService.fetchCurrenciesWithPaginationAndSorting(name, page, size, sortOrder));
        } catch (Exception e) {
            log.error("[GET] Error in method 'fetchCurrenciesWithPaginationAndSorting': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), request));
        }
    }

    @GetMapping("/minprice")
    public ResponseEntity<?> fetchCryptoWithTheMinPrice(@RequestParam String name, WebRequest request) {
        log.info("[GET] Request to resource '/api/cryptocurrencies/minprice', method 'fetchCryptoWithTheLowestPrice'");
        if (!name.isEmpty()) {
            return ResponseEntity.ok(cryptoService.fetchCryptoWithTheMinPrice(name));
        } else {
            log.warn("[GET] Warning in method 'fetchCryptoWithTheLowestPrice'");
            return ResponseEntity.badRequest().body(new MessageResponse("Provide cryptocurrency name in order to get the lowest price", request));
        }
    }

    @GetMapping("/maxprice")
    public ResponseEntity<?> fetchCryptoWithTheMaxPrice(@RequestParam String name, WebRequest request) {
        log.info("[GET] Request to resource '/api/cryptocurrencies/maxprice', method 'fetchCryptoWithTheMaxPrice'");
        if (!name.isEmpty()) {
            return ResponseEntity.ok(cryptoService.fetchCryptoWithTheMaxPrice(name));
        } else {
            log.warn("[GET] Warning in method 'fetchCryptoWithTheMaxPrice'");
            return ResponseEntity.badRequest().body(new MessageResponse("Provide cryptocurrency name in order to get the highest price", request));
        }
    }

    @GetMapping("/csv")
    public ResponseEntity<?> generateCSVReport(WebRequest request) {
        log.info("[GET] Request to resource '/api/cryptocurrencies/csv', method 'generateCSVReport'");
        try {
            String filename = "report_" + LocalDate.now() + ".csv";
            InputStreamResource file = new InputStreamResource(csvService.generateCSVReport());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .body(file);
        } catch (Exception e) {
            log.error("[GET] Error in method 'generateCSVReport': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), request));
        }
    }
}
