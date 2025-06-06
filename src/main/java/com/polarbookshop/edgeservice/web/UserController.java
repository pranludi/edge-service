package com.polarbookshop.edgeservice.web;

import com.polarbookshop.edgeservice.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("user")
    public Mono<User> getUser(
        @AuthenticationPrincipal OidcUser oidcUser
    ) {
        log.info("Fetching information about the currently authenticated user");
        var user = new User(
            oidcUser.getPreferredUsername(),
            oidcUser.getGivenName(),
            oidcUser.getFamilyName(),
            oidcUser.getClaimAsStringList("roles")
        );

        return Mono.just(user);
    }
}
