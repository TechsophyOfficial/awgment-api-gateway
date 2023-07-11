package com.techsophy.tsf.services.gateway.config;

import com.techsophy.idgenerator.IdGeneratorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig
{
    @Bean
    public IdGeneratorImpl idGeneratorImpl()
    {
        return new IdGeneratorImpl();
    }
}
