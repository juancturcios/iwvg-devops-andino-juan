package es.upm.miw.devops;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

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
}