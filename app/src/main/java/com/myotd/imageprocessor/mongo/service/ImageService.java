package com.myotd.imageprocessor.mongo.service;

import com.myotd.imageprocessor.mongo.model.Image;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ImageService {
    Mono<Image> findImageByImageId(String imageId);
    Mono<Image> save(Image image);
    Mono<Void> delete(Image imageId);
    Mono<Image> get(Image image);
    Flux<Image> getMany(List<String> idList);
    Mono<Image> update(Image image);
}