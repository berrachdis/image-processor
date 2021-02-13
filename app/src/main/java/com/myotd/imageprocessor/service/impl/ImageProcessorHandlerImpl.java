package com.myotd.imageprocessor.service.impl;

import com.myotd.imageprocessor.mongo.model.Image;
import com.myotd.imageprocessor.mongo.service.ImageService;
import com.myotd.imageprocessor.mongo.service.impl.ImageServiceImpl;
import com.myotd.imageprocessor.service.ImageProcessorHandler;
import com.myotd.imageprocessor.util.CommonConstantUtil;
import com.myotd.imageprocessor.util.CommonErrorHandlerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageProcessorHandlerImpl implements ImageProcessorHandler {
    private final ImageService imageService;

    @Override
    public Mono<ServerResponse> save(ServerRequest request) {
        final String userId = request.headers().header(CommonConstantUtil.HEADER_USER_ID).get(0);
        final String imageName = request.headers().header(CommonConstantUtil.HEADER_IMAGE_NAME).get(0);
        final String collectionId = request.headers().header(CommonConstantUtil.HEADER_COLLECTION_ID).get(0);
        final String imageId = ImageServiceImpl.generateId(imageName, userId);
        final String imagePath = ImageServiceImpl.getImagePath(userId, collectionId, imageName);
        final Image imageToSave = Image.builder().id(imageId).userID(userId).collectionID(collectionId).name(imageName)
                .path(imagePath).uploadTime(LocalDateTime.now()).build();
        return this.imageService.save(imageToSave)
                .log()
                .flatMap(image -> ServerResponse.ok().build())
                .onErrorResume(RuntimeException.class, CommonErrorHandlerUtil::handleError);
    }

    @Override
    public Mono<ServerResponse> get(ServerRequest request) {
        final String userId = request.headers().header(CommonConstantUtil.HEADER_USER_ID).get(0);
        final String imageName = request.headers().header(CommonConstantUtil.HEADER_IMAGE_NAME).get(0);
        final String imageId = ImageServiceImpl.generateId(imageName, userId);
        return this.imageService.findImageByImageId(imageId)
                .flatMap(image -> ServerResponse.ok().bodyValue(image))
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(RuntimeException.class, CommonErrorHandlerUtil::handleError);
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest request) {
        final String userId = request.headers().header(CommonConstantUtil.HEADER_USER_ID).get(0);
        final String imageName = request.headers().header(CommonConstantUtil.HEADER_IMAGE_NAME).get(0);
        final String imageId = ImageServiceImpl.generateId(imageName, userId);
        return this.imageService.findImageByImageId(imageId)
                .flatMap(imageService::delete)
                .flatMap(data -> ServerResponse.ok().build())
                .onErrorResume(RuntimeException.class, CommonErrorHandlerUtil::handleError);
    }

    @Override
    public Mono<ServerResponse> createDir(ServerRequest request) {
        final String userId = request.headers().header(CommonConstantUtil.HEADER_USER_ID).get(0);
        final String collectionId = request.headers().header(CommonConstantUtil.HEADER_COLLECTION_ID).get(0);
        final String dirPathToCreate = ImageServiceImpl.buildDirPath(userId, collectionId);
        return ServerResponse.ok().build();
    }

    @Override
    public Mono<ServerResponse> exists(ServerRequest request) {
        final String userId = request.headers().header(CommonConstantUtil.HEADER_USER_ID).get(0);
        final String imageName = request.headers().header(CommonConstantUtil.HEADER_IMAGE_NAME).get(0);
        final String imageId = ImageServiceImpl.generateId(imageName, userId);
        return this.imageService.findImageByImageId(imageId)
                .flatMap(image -> ServerResponse.ok().bodyValue(image))
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(RuntimeException.class, CommonErrorHandlerUtil::handleError);
    }
}
