package com.techsophy.tsf.services.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ServerWebExchange;
import static constants.GatewayConstants.*;

@ActiveProfiles(TEST_ACTIVE_PROFILE)
@SpringBootTest(properties = {"CORS_ALLOWEDORIGINS=VALUE", "ADD_ALLOWEDMETHOD=VALUE2", "ADD_ALLOWEDHEADER=VALUE3"})
class TenantAuthenticationManagerResolverTest {

    @Mock
    ServerWebExchange serverWebExchange;
    @Mock
    ServerHttpRequest serverHttpRequest;
    @Mock
    HttpHeaders httpHeaders;
    @Mock
    HttpMessage httpMessage;

    static String idToken = "Bearer eyJraWQiOiIxZTlnZGs3IiwiYWxnIjoiUlMyNTYifQ.ewogImlzcyI6ICJodHRwOi8vc2VydmVyLmV4YW1wbGUuY29tIiwKICJzdWIiOiAiMjQ4Mjg5NzYxMDAxIiwKICJhdWQiOiAiczZCaGRSa3F0MyIsCiAibm9uY2UiOiAibi0wUzZfV3pBMk1qIiwKICJleHAiOiAxMzExMjgxOTcwLAogImlhdCI6IDEzMTEyODA5NzAsCiAibmFtZSI6ICJKYW5lIERvZSIsCiAiZ2l2ZW5fbmFtZSI6ICJKYW5lIiwKICJmYW1pbHlfbmFtZSI6ICJEb2UiLAogImdlbmRlciI6ICJmZW1hbGUiLAogImJpcnRoZGF0ZSI6ICIwMDAwLTEwLTMxIiwKICJlbWFpbCI6ICJqYW5lZG9lQGV4YW1wbGUuY29tIiwKICJwaWN0dXJlIjogImh0dHA6Ly9leGFtcGxlLmNvbS9qYW5lZG9lL21lLmpwZyIKfQ.rHQjEmBqn9Jre0OLykYNnspA10Qql2rvx4FsD00jwlB0Sym4NzpgvPKsDjn_wMkHxcp6CilPcoKrWHcipR2iAjzLvDNAReF97zoJqq880ZD1bwY82JDauCXELVR9O6_B0w3K-E7yM2macAAgNCUwtik6SjoSUZRcf-O5lygIyLENx882p6MtmwaL1hd6qn5RZOQ0TLrOYu0532g9Exxcm-ChymrB4xLykpDj3lUivJt63eEGGN6DH5K6o33TcxkIjNrCD4XB1CKKumZvCedgHHF3IAK4dVEDSUoGlH9z4pP_eWYNXvqQOjGs-rDaQzUHl6cQQWNiDpWOl_lxXjQEvQ";

    @InjectMocks
    TenantAuthenticationManagerResolver tenantAuthenticationManagerResolver;

    @BeforeEach
    public void setUp()
    {
        ReflectionTestUtils.setField(tenantAuthenticationManagerResolver,"keycloakIssuerUri","https://keycloak-tsplatform.techsophy.com/auth/realms/");
    }

    @Order(1)
    @Test
    void string2JSONMapTest() {

        String JSONString = "{name:Techsophy, project:AWGMENT}";
        Assertions.assertThrows(JsonProcessingException.class, () ->
                TenantAuthenticationManagerResolver.string2JSONMap(JSONString));
    }

    @Order(1)
    @Test
    void string2JSONMapTestWithInvalidInput() {
        Assertions.assertThrows(JsonProcessingException.class, () ->
                TenantAuthenticationManagerResolver.string2JSONMap("invalid JSON"));
    }

    @Order(2)
    @Test
    void getIssuerFromTokenTest() throws JsonProcessingException{

        Assertions.assertEquals("server.example.com",
                TenantAuthenticationManagerResolver.getIssuerFromToken(idToken));

    }

    @Order(3)
    @Test
    void resolveTest(){

//        Mockito.when(httpMessage.getHeaders()).thenReturn(httpHeaders);
        Mockito.when(serverWebExchange.getRequest()).thenReturn(serverHttpRequest);
        Mockito.when(serverHttpRequest.getHeaders()).thenReturn(httpHeaders);
        Mockito.when(httpHeaders.getFirst("Authorization")).thenReturn(idToken);

        Assertions.assertThrows(IllegalArgumentException.class,()->
                tenantAuthenticationManagerResolver.resolve(serverWebExchange));
    }
}
