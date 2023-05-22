package com.techsophy.tsf.services.gateway.config;

import com.techsophy.tsf.services.gateway.model.SecurityDisableModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityDisableModel securityDisableModel;
    @Value("${CORS_ALLOWEDORIGINS}")
    private String corsAllowedOrigins;
    @Value("${ADD_ALLOWEDMETHOD}")
    private String addAllowedMethod;
    @Value("${ADD_ALLOWEDHEADER}")
    private String addAllowedHeader;



    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList(corsAllowedOrigins));
        corsConfig.setMaxAge(8000L);
        corsConfig.addAllowedMethod(addAllowedMethod);
        corsConfig.addAllowedHeader(addAllowedHeader);
        corsConfig.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            ReactiveAuthenticationManagerResolver<ServerWebExchange> authenticationManagerResolver
//            ServerOAuth2AuthorizationRequestResolver requestResolver
//            ServerAuthorizationRequestRepository requestRepository
    ) {
        String[] res = securityDisableModel.getBaseUrl().toArray(new String[0]);
        http.csrf().disable();
        http.cors().configurationSource(corsConfigurationSource());
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(res).permitAll()
                .anyExchange().authenticated())
                .oauth2Login(Customizer.withDefaults()
//                        oAuth2LoginSpec -> {
////                    oAuth2LoginSpec.authorizationRequestResolver(new AwgmentOAuthRequestResolver(repository));
//            oAuth2LoginSpec.authorizationRequestResolver();
////            oAuth2LoginSpec.authorizationRequestRepository(requestRepository);
//            }
        );
        http.oauth2ResourceServer(oauth2 -> oauth2.authenticationManagerResolver(authenticationManagerResolver));

        return http.build();
    }

//    @Bean
//    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
//        return new KeycloakRealmRepository();
//    }

}
