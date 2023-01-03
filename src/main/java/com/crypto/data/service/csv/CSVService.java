package com.crypto.data.service.csv;

import java.io.ByteArrayInputStream;

/**
 * The CSVService interface is required to create CSVServiceImpl {@link CSVServiceImpl}
 */

public interface CSVService {
    ByteArrayInputStream generateCSVReport();
}
