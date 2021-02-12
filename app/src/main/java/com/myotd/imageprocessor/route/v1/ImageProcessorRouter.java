package com.myotd.imageprocessor.route.v1;

import com.myotd.imageprocessor.service.ImageProcessorHandler;
import com.myotd.imageprocessor.service.impl.ImageProcessorHandlerImpl;
import com.myotd.imageprocessor.util.CommonFilterUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.headers;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;

@Component
public class ImageProcessorRouter {
    public static final String IMAGE_PROCESSOR_ROUTE = "/v1/image";

    @Bean
    public RouterFunction<ServerResponse> route(ImageProcessorHandler imageProcessorHandler) {
        return RouterFunctions.route()
                .nest(path(IMAGE_PROCESSOR_ROUTE), builder -> builder
                        .GET("", headers(CommonFilterUtil.IS_VALID_HEADER_USER_ID_AND_IMAGE_ID), imageProcessorHandler::get)
                        .DELETE("", headers(CommonFilterUtil.IS_VALID_HEADER_USER_ID_AND_IMAGE_ID), imageProcessorHandler::delete)
                        .PUT("", headers(CommonFilterUtil.IS_VALID_HEADER_USER_ID_AND_COLLECTION_ID_AND_IMAGE_NAME).and(accept(MediaType.MULTIPART_FORM_DATA)), imageProcessorHandler::save)
                        .POST("", headers(CommonFilterUtil.IS_VALID_HEADER_USER_ID_COLLECTION_ID), imageProcessorHandler::createDir)
                        .GET("/exist", headers(CommonFilterUtil.IS_VALID_DIR_PATH_NAME), imageProcessorHandler::exists))
                .build();
    }
}