package com.myotd.imageprocessor.storage.impl;

import com.github.sardine.Sardine;
import com.myotd.imageprocessor.domain.enums.StorageStrategyEnum;
import com.myotd.imageprocessor.exception.WebdavClientException;
import com.myotd.imageprocessor.property.ImageProcessorProperties;
import com.myotd.imageprocessor.storage.StorageProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebDavProxyImpl implements StorageProxy {
    private final Map<ImageProcessorProperties.ConnectionProperties, Sardine> sardineClients;

    @Override
    public Mono<Void> put(String fileName, InputStream data) {
        return Mono.fromRunnable(() -> {
            try {
                for (Map.Entry<ImageProcessorProperties.ConnectionProperties, Sardine> client : sardineClients.entrySet()) {
                    client.getValue().put(client.getKey().getHost() + fileName, data);
                }
            } catch (IOException e) {
                log.error("Error occurred while put the Document {}", fileName);
                throw new WebdavClientException(e.getCause());
            }
        }).log(String.format("Image = %s saved", fileName)).then();
    }

    @Override
    public Mono<Void> delete(String fileName) {
        return Mono.fromRunnable(() -> {
            try {
                for (Map.Entry<ImageProcessorProperties.ConnectionProperties, Sardine> client : sardineClients.entrySet()) {
                    client.getValue().delete(client.getKey().getHost() + fileName);
                }
            } catch (IOException e) {
                log.error("Error occurred while deleting the document {}", fileName);
                throw new WebdavClientException(e.getCause());
            }
        }).log(String.format("Image = %s deleted", fileName)).then();
    }

    @Override
    public Mono<Void> createDirectory(String dirName) {
        return Mono.fromRunnable(() -> {
            try {
                for (Map.Entry<ImageProcessorProperties.ConnectionProperties, Sardine> client : sardineClients.entrySet()) {
                    client.getValue().createDirectory(client.getKey().getHost() + dirName);
                }
            } catch (IOException e) {
                log.error("Error occurred while creating the document {}", dirName);
                throw new WebdavClientException(e.getCause());
            }
        }).log(String.format("Directory = %s created", dirName)).then();
    }

    @Override
    public Mono<Boolean> exists(String path) {
        return Mono.fromSupplier(() -> {
            try {
                for (Map.Entry<ImageProcessorProperties.ConnectionProperties, Sardine> client : sardineClients.entrySet()) {
                    if (!client.getValue().exists(client.getKey().getHost() + path)) {
                        return false;
                    }
                }
                return true;
            } catch (IOException e) {
                log.error("Error occurred while check the existence of the the given path = {}", path);
                throw new WebdavClientException(e.getCause());
            }
        });
    }

    @Override
    public Mono<InputStream> get(String url) {
        return Mono.fromSupplier(() -> {
            InputStream is = null;
            try {
                for (Map.Entry<ImageProcessorProperties.ConnectionProperties, Sardine> client : sardineClients.entrySet()) {
                    is = client.getValue().get(client.getKey().getHost() + url);
                    if (is != null) {
                        break;
                    }
                }
            } catch (IOException e) {
                log.error("Error occurred while getting the document from the url = {}", url);
                throw new WebdavClientException(e.getCause());
            }
            return is;
        });
    }

    @Override
    public StorageStrategyEnum getStorageType() {
        return StorageStrategyEnum.WEBDAV;
    }
}