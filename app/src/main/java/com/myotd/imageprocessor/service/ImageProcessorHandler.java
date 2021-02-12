package com.myotd.imageprocessor.service;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ImageProcessorHandler {
    Mono<ServerResponse> save(ServerRequest request);
    Mono<ServerResponse> get(ServerRequest request);
    Mono<ServerResponse> delete(ServerRequest request);
    Mono<ServerResponse> createDir(ServerRequest request);
    Mono<ServerResponse> exists(ServerRequest request);
}
