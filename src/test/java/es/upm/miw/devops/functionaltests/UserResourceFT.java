package es.upm.miw.devops.functionaltests;

import es.upm.miw.devops.User;
import es.upm.miw.devops.rest.UserResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class UserResourceFT {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testReadUserById() {
        webTestClient.get()
                .uri(UserResource.USERS + "/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> {
                    assertThat(user.getId()).isEqualTo("1");
                    assertThat(user.getFirstName()).isEqualTo("Juan");
                    assertThat(user.getFamilyName()).isEqualTo("Andino");
                    assertThat(user.getCity()).isEqualTo("Madrid");
                    assertThat(user.getActive()).isTrue();
                });
    }

    @Test
    void testReadUserByIdNotFound() {
        webTestClient.get()
                .uri(UserResource.USERS + "/no-existe")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User not found: no-existe")
                .jsonPath("$.code").isEqualTo(404);
    }
}