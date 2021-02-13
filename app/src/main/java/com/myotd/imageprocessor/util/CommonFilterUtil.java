package com.myotd.imageprocessor.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.function.Predicate;

@UtilityClass
public class CommonFilterUtil {

    /**
     * Predicate used to validate UserID and ImageID headers
     */
    public static final Predicate<ServerRequest.Headers> IS_VALID_HEADER_USER_ID_AND_IMAGE_NAME = headers ->
            !CollectionUtils.isEmpty(headers.header(CommonConstantUtil.HEADER_USER_ID))
                    && !CollectionUtils.isEmpty(headers.header(CommonConstantUtil.HEADER_IMAGE_NAME));

    /**
     * Predicate used to validate UserID and CollectionID headers
     */
    public static final Predicate<ServerRequest.Headers> IS_VALID_HEADER_USER_ID_COLLECTION_ID = headers ->
            !CollectionUtils.isEmpty(headers.header(CommonConstantUtil.HEADER_USER_ID))
                    && !CollectionUtils.isEmpty(headers.header(CommonConstantUtil.HEADER_COLLECTION_ID));

    /**
     * Predicate used to validate UserID, CollectionID and ImageName headers
     */
    public static final Predicate<ServerRequest.Headers> IS_VALID_HEADER_USER_ID_AND_COLLECTION_ID_AND_IMAGE_NAME =
        headers -> IS_VALID_HEADER_USER_ID_COLLECTION_ID.test(headers)
                && !CollectionUtils.isEmpty(headers.header(CommonConstantUtil.HEADER_IMAGE_NAME));

    public static final Predicate<ServerRequest.Headers> IS_VALID_DIR_PATH_NAME = headers ->
            !CollectionUtils.isEmpty(headers.header(CommonConstantUtil.HEADER_DIR_PATH_NAME));
}
