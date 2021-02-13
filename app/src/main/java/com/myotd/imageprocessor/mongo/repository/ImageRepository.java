package com.myotd.imageprocessor.mongo.repository;


import com.myotd.imageprocessor.mongo.model.Image;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
public interface ImageRepository extends ReactiveMongoRepository<Image, String> {
}