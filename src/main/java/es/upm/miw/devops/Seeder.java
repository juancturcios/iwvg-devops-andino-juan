package es.upm.miw.devops;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class Seeder {

    private final UserRepository userRepository;

    public Seeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    void seed() {
        this.userRepository.save(new User("1", "Juan", "Andino", "juan.andino@example.com", "12345678A",
                "C/ Mayor 1", "Madrid", "Madrid", "28001", true));
        this.userRepository.save(new User("2", "Ana", "Garcia", "ana.garcia@example.com", "87654321B",
                "Av. Diagonal 2", "Barcelona", "Barcelona", "08001", true));
        this.userRepository.save(new User("3", "Luis", "Gutierrez", "luis.gutierrez@example.com", "11223344C",
                "C/ Sol 3", "Sevilla", "Sevilla", "41001", false));
    }
}