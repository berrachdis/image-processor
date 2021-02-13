package com.myotd.imageprocessor.storage.configuration;
import com.github.sardine.Sardine;
import com.github.sardine.impl.SardineImpl;
import com.myotd.imageprocessor.property.ImageProcessorProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class WebDavConfig {

    @Bean
    public Map<ImageProcessorProperties.ConnectionProperties, Sardine> sardine(ImageProcessorProperties properties) {
        // TODO : Add proxy here
        Map<ImageProcessorProperties.ConnectionProperties, Sardine> clients = new HashMap<>();
                properties.getWebdav().getConnections().entrySet()
                .stream()
                .map(entry -> entry.getValue())
                .filter(ImageProcessorProperties.ConnectionProperties::isEnabled)
                .forEach(client -> clients.put(client, new SardineImpl(client.getUser(), client.getPassword())));
        return clients;
    }
}
