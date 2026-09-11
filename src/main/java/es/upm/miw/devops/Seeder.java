package es.upm.miw.devops;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class Seeder {

    private final UsersDatabase usersDatabase;

    public Seeder(UsersDatabase usersDatabase) {
        this.usersDatabase = usersDatabase;
    }

    @PostConstruct
    void seed() {
        this.usersDatabase.save(new User("1", "Juan", "Andino"));
        this.usersDatabase.save(new User("2", "Ana", "Garcia"));
        this.usersDatabase.save(new User("3", "Luis", "Gutierrez"));
    }
}