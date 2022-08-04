package com.techsophy.tsf.services.gateway.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
@ConfigurationProperties("security.disabled")
@Getter
@Setter
public class SecurityDisableModel {

    List<String> baseUrl;
}
