package com.myotd.imageprocessor.property;

import com.myotd.imageprocessor.domain.enums.StorageStrategyEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;
import java.util.Map;
import java.util.TreeMap;


@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
@Configuration
@Validated
@Getter @Setter @ToString
public class ImageProcessorProperties {
    private StorageStrategyEnum storageStrategy;
    private final WebDavProperties webdav = new WebDavProperties();

    @Getter
    @Setter
    public static class WebDavProperties {
        private boolean enabled;
        private final ClientProperties client = new ClientProperties();
        private final Map<String, ConnectionProperties> connections = new TreeMap<>();
    }

    @Getter
    @Setter
    public static class ClientProperties {
        private String targetPath;
        private int timeout;
    }

    @Getter
    @Setter
    public static class ConnectionProperties {
        @Pattern(regexp = ".*\\/")
        private String host;
        private String user;
        private String password;
        private boolean enabled;
        private String proxy;
    }
}
