package com.myotd.imageprocessor.mongo.service.impl;

import com.myotd.imageprocessor.mongo.model.Image;
import com.myotd.imageprocessor.mongo.repository.ImageRepository;
import com.myotd.imageprocessor.mongo.service.ImageService;
import com.myotd.imageprocessor.util.CommonConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public Mono<Image> findImageByImageId(String imageId) {
        // String userId, String imageName
        log.info("Find by imageId = {}", imageId);
        return this.imageRepository.findById(imageId);
    }

    @Override
    public Mono<Image> save(Image image) {
        log.info("Save new {}", image);
        return this.imageRepository.save(image);
    }

    @Override
    public Mono<Void> delete(Image image) {
        log.info("Delete {}", image);
        return this.imageRepository.delete(image);
    }

    @Override
    public Mono<Image> get(Image image) {
        log.info("Get {}", image);
        return this.imageRepository.findById(image.getId());
    }

    @Override
    public Flux<Image> getMany(List<String> idList) {
        return this.imageRepository.findAllById(idList);
    }

    @Override
    public Mono<Image> update(Image image) {
        log.info("Update the image = {}", image);
        return this.imageRepository.save(image);
    }

    public static String generateId(String imageName, String userId) {
        return UUID.nameUUIDFromBytes((imageName + userId).getBytes()).toString();
    }

    public static String getImagePath(String userId, String collectionId, String imgName)  {
        final String basePath = buildDirPath(userId, collectionId);
        return  basePath + imgName;
    }

    /**
     * Method used to build directory path from {@code args}
     * @param args params
     * @return directory path
     */
    public static String buildDirPath(String ...args) {
        String dirPath = "";
        for (String l : args) {
            dirPath += l + CommonConstantUtil.DIR_SEPARATOR;
        }
        return dirPath;
    }
}
