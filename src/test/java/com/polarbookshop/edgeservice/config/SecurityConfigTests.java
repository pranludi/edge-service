package com.polarbookshop.edgeservice.config;

import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest
@Import(SecurityConfig.class)
class SecurityConfigTests {

    @Autowired
    WebTestClient webClient;

    @MockitoBean
    ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Test
    void whenLogoutAuthenticatedAndWithCsrfTokenThen302() {
        when(clientRegistrationRepository.findByRegistrationId("test"))
            .thenReturn(Mono.just(testClientRegistration()));

        webClient
            .mutateWith(SecurityMockServerConfigurers.mockOidcLogin())
            .mutateWith(SecurityMockServerConfigurers.csrf())
            .post()
            .uri("/logout")
            .exchange()
            .expectStatus()
            .isFound();
    }

    private ClientRegistration testClientRegistration() {
        return ClientRegistration
            .withRegistrationId("test")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .clientId("test")
            .authorizationUri("https://sso.polarbookshop.com/auth")
            .tokenUri("https://sso.polarbookshop.com/token")
            .redirectUri("https://polarbookshop.com")
            .build();
    }
}
