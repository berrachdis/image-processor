package com.myotd.imageprocessor.service.impl;

import com.myotd.imageprocessor.service.ImageProcessorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageProcessorHandlerImpl implements ImageProcessorHandler {


    @Override
    public Mono<ServerResponse> save(ServerRequest request) {
        return ServerResponse.ok().build();
    }

    @Override
    public Mono<ServerResponse> get(ServerRequest request) {
        return ServerResponse.ok().build();
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest request) {
        return ServerResponse.ok().build();
    }

    @Override
    public Mono<ServerResponse> createDir(ServerRequest request) {
        return ServerResponse.ok().build();
    }

    @Override
    public Mono<ServerResponse> exists(ServerRequest request) {
        return ServerResponse.ok().build();
    }
}
