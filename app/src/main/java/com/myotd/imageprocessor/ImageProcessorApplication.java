package com.myotd.imageprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = {"com.myotd.imageprocessor.mongo.repository"})
public class ImageProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageProcessorApplication.class, args);
    }

}
