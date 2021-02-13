package com.myotd.imageprocessor.storage.impl;

import com.myotd.imageprocessor.domain.enums.StorageStrategyEnum;
import com.myotd.imageprocessor.exception.UnsupportedStorageProxyException;
import com.myotd.imageprocessor.storage.StorageProxy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.InputStream;

@Service
public class AmazonStorageProxy implements StorageProxy {
    private static final String AMAZON_PROXY_NOT_SUPPORTED = "AmazonStorageProxy not supported yet";

    @Override
    public Mono<Void> createDirectory(String dirName) {
        throw new UnsupportedStorageProxyException(AMAZON_PROXY_NOT_SUPPORTED);
    }

    @Override
    public Mono<Boolean> exists(String fileName) {
        throw new UnsupportedStorageProxyException(AMAZON_PROXY_NOT_SUPPORTED);
    }

    @Override
    public Mono<InputStream> get(String url) {
        throw new UnsupportedStorageProxyException(AMAZON_PROXY_NOT_SUPPORTED);
    }

    @Override
    public Mono<Void> put(String fileName, InputStream data) {
        throw new UnsupportedStorageProxyException(AMAZON_PROXY_NOT_SUPPORTED);
    }

    @Override
    public Mono<Void> delete(String fileName) {
        throw new UnsupportedStorageProxyException(AMAZON_PROXY_NOT_SUPPORTED);
    }

    @Override
    public StorageStrategyEnum getStorageType() {
        return StorageStrategyEnum.AMAZON_S3;
    }
}
