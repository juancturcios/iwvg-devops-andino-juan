package es.upm.miw.devops;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testReadExistingUser() {
        User user = this.userService.read("1");
        assertThat(user.getId()).isEqualTo("1");
        assertThat(user.getFirstName()).isEqualTo("Juan");
        assertThat(user.getFamilyName()).isEqualTo("Andino");
        assertThat(user.getEmail()).isEqualTo("juan.andino@example.com");
        assertThat(user.getActive()).isTrue();
    }

    @Test
    void testReadNotExistingUser() {
        assertThatThrownBy(() -> this.userService.read("no-existe"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteExistingUser() {
        this.userService.delete("2");
        assertThatThrownBy(() -> this.userService.read("2"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteNotExistingUser() {
        assertThatThrownBy(() -> this.userService.delete("no-existe"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testUpdateActiveExistingUser() {
        boolean original = this.userService.read("3").getActive();
        User user = this.userService.updateActive("3");
        assertThat(user.getActive()).isEqualTo(!original);
        user = this.userService.updateActive("3");
        assertThat(user.getActive()).isEqualTo(original);
    }

    @Test
    void testUpdateActiveNotExistingUser() {
        assertThatThrownBy(() -> this.userService.updateActive("no-existe"))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testFindAll() {
        List<User> users = this.userService.find(null, null, null);
        assertThat(users).extracting(User::getId).contains("1", "3");
    }

    @Test
    void testFindByFirstName() {
        List<User> users = this.userService.find("Juan", null, null);
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getId()).isEqualTo("1");
    }

    @Test
    void testFindByFamilyName() {
        List<User> users = this.userService.find(null, "Gutierrez", null);
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getId()).isEqualTo("3");
    }

    @Test
    void testFindByBillableTrue() {
        List<User> users = this.userService.find(null, null, true);
        assertThat(users).isNotEmpty();
        assertThat(users).allMatch(User::isBillable);
        assertThat(users).extracting(User::getId).contains("1", "3");
    }

    @Test
    void testFindByBillableFalse() {
        assertThat(this.userService.find(null, null, false)).isEmpty();
    }

    @Test
    void testFindByFirstNameAndBillable() {
        List<User> users = this.userService.find("Luis", null, true);
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getId()).isEqualTo("3");
    }
}