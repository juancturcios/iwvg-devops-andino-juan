package es.upm.miw.devops.functionaltests;

import es.upm.miw.devops.User;
import es.upm.miw.devops.UserActive;
import es.upm.miw.devops.rest.UserResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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

    @Test
    void testDeleteUserById() {
        webTestClient.delete()
                .uri(UserResource.USERS + "/2")
                .exchange()
                .expectStatus().isNoContent();
        webTestClient.get()
                .uri(UserResource.USERS + "/2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteUserByIdNotFound() {
        webTestClient.delete()
                .uri(UserResource.USERS + "/no-existe")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateActiveUserById() {
        User first = webTestClient.put()
                .uri(UserResource.USERS + "/3/active")
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .returnResult().getResponseBody();
        webTestClient.put()
                .uri(UserResource.USERS + "/3/active")
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> {
                    assertThat(user.getId()).isEqualTo("3");
                    assertThat(user.getActive()).isEqualTo(!first.getActive());
                });
    }

    @Test
    void testUpdateActiveUserByIdNotFound() {
        webTestClient.put()
                .uri(UserResource.USERS + "/no-existe/active")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testSearchAll() {
        webTestClient.get()
                .uri(UserResource.USERS + "/search")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> assertThat(users).extracting(User::getId).contains("1", "3"));
    }

    @Test
    void testSearchByFirstName() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(UserResource.USERS + "/search")
                        .queryParam("firstName", "Juan").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> {
                    assertThat(users).hasSize(1);
                    assertThat(users.get(0).getId()).isEqualTo("1");
                });
    }

    @Test
    void testSearchByFamilyName() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(UserResource.USERS + "/search")
                        .queryParam("familyName", "Gutierrez").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> {
                    assertThat(users).hasSize(1);
                    assertThat(users.get(0).getId()).isEqualTo("3");
                });
    }

    @Test
    void testSearchByBillableTrue() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(UserResource.USERS + "/search")
                        .queryParam("billable", "true").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> {
                    assertThat(users).isNotEmpty();
                    assertThat(users).allMatch(User::isBillable);
                    assertThat(users).extracting(User::getId).contains("1", "3");
                });
    }

    @Test
    void testSearchByBillableFalse() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(UserResource.USERS + "/search")
                        .queryParam("billable", "false").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> assertThat(users).isEmpty());
    }

    @Test
    void testSearchByFirstNameAndBillable() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(UserResource.USERS + "/search")
                        .queryParam("firstName", "Luis")
                        .queryParam("billable", "true").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> {
                    assertThat(users).hasSize(1);
                    assertThat(users.get(0).getId()).isEqualTo("3");
                });
    }

    @Test
    void testUpdateUserById() {
        webTestClient.put()
                .uri(UserResource.USERS + "/3")
                .bodyValue(new User("3", "Luis", "Gutierrez", "luis.gutierrez@example.com", "11223344C",
                        "C/ Sol 3", "Valencia", "Valencia", "46001", false))
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(user -> {
                    assertThat(user.getId()).isEqualTo("3");
                    assertThat(user.getCity()).isEqualTo("Valencia");
                    assertThat(user.getProvince()).isEqualTo("Valencia");
                    assertThat(user.isBillable()).isTrue();
                });
    }

    @Test
    void testUpdateUserByIdNotFound() {
        webTestClient.put()
                .uri(UserResource.USERS + "/no-existe")
                .bodyValue(new User())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateActiveList() {
        webTestClient.patch()
                .uri(UserResource.USERS)
                .bodyValue(List.of(new UserActive("1", false), new UserActive("3", true)))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .value(users -> {
                    assertThat(users).hasSize(2);
                    assertThat(users).extracting(User::getId).containsExactlyInAnyOrder("1", "3");
                    assertThat(users).filteredOn(user -> user.getId().equals("1"))
                            .singleElement()
                            .extracting(User::getActive)
                            .isEqualTo(false);
                    assertThat(users).filteredOn(user -> user.getId().equals("3"))
                            .singleElement()
                            .extracting(User::getActive)
                            .isEqualTo(true);
                });
        webTestClient.patch()
                .uri(UserResource.USERS)
                .bodyValue(List.of(new UserActive("1", true)))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testUpdateActiveListNotFound() {
        webTestClient.patch()
                .uri(UserResource.USERS)
                .bodyValue(List.of(new UserActive("no-existe", true)))
                .exchange()
                .expectStatus().isNotFound();
    }
}