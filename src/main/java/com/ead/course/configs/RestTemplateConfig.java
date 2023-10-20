package com.ead.course.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @LoadBalanced
    // Essa configuração permite que você injete um RestTemplate em outras partes do seu código, facilitando a
    // comunicação com serviços externos por meio de requisições HTTP.
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
