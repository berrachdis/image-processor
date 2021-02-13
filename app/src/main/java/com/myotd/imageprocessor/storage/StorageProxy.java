package com.myotd.imageprocessor.storage;

import com.myotd.imageprocessor.domain.enums.StorageStrategyEnum;
import reactor.core.publisher.Mono;

import java.io.InputStream;

public interface StorageProxy {
    Mono<Void> createDirectory(String dirName);
    Mono<Boolean> exists(String fileName);
    Mono<InputStream> get(String url);
    Mono<Void> put(String fileName, InputStream data);
    Mono<Void> delete(String fileName);
    StorageStrategyEnum getStorageType();
}
