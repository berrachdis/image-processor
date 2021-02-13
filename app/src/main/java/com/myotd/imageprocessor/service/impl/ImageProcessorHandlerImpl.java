package com.myotd.imageprocessor.service.impl;

import com.myotd.imageprocessor.mongo.model.Image;
import com.myotd.imageprocessor.mongo.service.ImageService;
import com.myotd.imageprocessor.mongo.service.impl.ImageServiceImpl;
import com.myotd.imageprocessor.property.ImageProcessorProperties;
import com.myotd.imageprocessor.service.ImageProcessorHandler;
import com.myotd.imageprocessor.storage.StorageFactory;
import com.myotd.imageprocessor.storage.StorageProxy;
import com.myotd.imageprocessor.util.CommonConstantUtil;
import com.myotd.imageprocessor.util.CommonErrorHandlerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.io.SequenceInputStream;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ImageProcessorHandlerImpl implements ImageProcessorHandler {
    private final ImageService imageService;
    private final StorageProxy storageProxy;

    public ImageProcessorHandlerImpl(final ImageProcessorProperties imageProcessorProperties,
                                     final StorageFactory storageFactory,
                                     final ImageService imageService) {
        this.storageProxy = storageFactory.findStorageProxy(imageProcessorProperties.getStorageStrategy());
        this.imageService = imageService;
    }

    @Override
    public Mono<ServerResponse> save(ServerRequest request) {
        final String userId = request.headers().header(CommonConstantUtil.HEADER_USER_ID).get(0);
        final String imageName = request.headers().header(CommonConstantUtil.HEADER_IMAGE_NAME).get(0);
        final String collectionId = request.headers().header(CommonConstantUtil.HEADER_COLLECTION_ID).get(0);
        final String imageId = ImageServiceImpl.generateId(imageName, userId);
        final String imagePath = ImageServiceImpl.getImagePath(userId, collectionId, imageName);
        final Image imageToSave = Image.builder().id(imageId).userID(userId).collectionID(collectionId).name(imageName)
                .path(imagePath).uploadTime(LocalDateTime.now()).build();
        final Mono<Image> savedImageDetails = this.imageService.save(imageToSave);
        final Mono<Void> savedImageStorage = request.multipartData()
                .map(data -> data.get(CommonConstantUtil.HEADER_FILENAME))
                .flatMapMany(Flux::fromIterable)
                .cast(FilePart.class)
                .flatMap(filePart -> filePart.content().reduce(new InputStream() {
                    @Override
                    public int read() {
                        return -1;
                    }
                }, (InputStream s, DataBuffer d) -> new SequenceInputStream(s, d.asInputStream())))
                .flatMap(is -> this.storageProxy.put(imagePath, is))
                .then();

        return Mono.when(savedImageDetails, savedImageStorage)
                .flatMap(data -> ServerResponse.ok().build())
                .onErrorResume(RuntimeException.class, CommonErrorHandlerUtil::handleError);
    }

    @Override
    public Mono<ServerResponse> get(ServerRequest request) {
        final String userId = request.headers().header(CommonConstantUtil.HEADER_USER_ID).get(0);
        final String imageName = request.headers().header(CommonConstantUtil.HEADER_IMAGE_NAME).get(0);
        final String imageId = ImageServiceImpl.generateId(imageName, userId);
        return this.imageService.findImageByImageId(imageId)
                .flatMap(image -> this.storageProxy.get(image.getPath()))
                .flatMap(is -> ServerResponse.ok().contentType(MediaType.IMAGE_JPEG).bodyValue(new InputStreamResource(is)))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(RuntimeException.class, CommonErrorHandlerUtil::handleError);
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest request) {
        final String userId = request.headers().header(CommonConstantUtil.HEADER_USER_ID).get(0);
        final String imageName = request.headers().header(CommonConstantUtil.HEADER_IMAGE_NAME).get(0);
        final String imageId = ImageServiceImpl.generateId(imageName, userId);

        return this.imageService.findImageByImageId(imageId)
                .flatMap(image -> this.storageProxy.delete(image.getPath() + imageId).thenReturn(image))
                .flatMap(imageService::delete)
                .flatMap(data -> ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(RuntimeException.class, CommonErrorHandlerUtil::handleError);
    }

    @Override
    public Mono<ServerResponse> createDir(ServerRequest request) {
        // TODO : store the dir path
        final String userId = request.headers().header(CommonConstantUtil.HEADER_USER_ID).get(0);
        final String collectionId = request.headers().header(CommonConstantUtil.HEADER_COLLECTION_ID).get(0);
        final String dirPathToCreate = ImageServiceImpl.buildDirPath(userId, collectionId);
        return this.storageProxy.createDirectory(dirPathToCreate)
                .flatMap(data -> ServerResponse.ok().build())
                .onErrorResume(RuntimeException.class, CommonErrorHandlerUtil::handleError);
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
