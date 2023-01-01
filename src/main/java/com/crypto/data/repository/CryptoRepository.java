package com.crypto.data.repository;

import com.crypto.data.entity.Crypto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptoRepository extends MongoRepository<Crypto, String> {

    Page<Crypto> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Crypto> findAll();

    Crypto findByName(String name);
}
