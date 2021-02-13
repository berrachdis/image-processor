package com.myotd.imageprocessor.storage;

import com.myotd.imageprocessor.domain.enums.StorageStrategyEnum;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StorageFactory {
    private final Map<StorageStrategyEnum, StorageProxy> storageProxyMap;

    public StorageFactory(Set<StorageProxy> storageProxySet) {
        this.storageProxyMap = createStorageProxy(storageProxySet);
    }

    private Map<StorageStrategyEnum, StorageProxy> createStorageProxy(Set<StorageProxy> storageProxySet) {
        return storageProxySet.stream()
                .collect(Collectors.toMap(StorageProxy::getStorageType, a -> a));
    }

    public StorageProxy findStorageProxy(StorageStrategyEnum storageStrategy) {
        return this.storageProxyMap.get(storageStrategy);
    }
}
