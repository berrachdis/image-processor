package com.myotd.imageprocessor.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@UtilityClass
public class CommonErrorHandlerUtil {

    /**
     * Method used to handle exception
     * @param e instance of {@literal RuntimeException}
     * @return return instance of {@literal ServerResponse}
     */
    public static Mono<ServerResponse> handleError(RuntimeException e) {
        log.error(e.getMessage());
        return ServerResponse.badRequest().build();
    }
}
