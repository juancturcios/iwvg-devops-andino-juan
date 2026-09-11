package es.upm.miw.devops;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UsersDatabase {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    public void save(User user) {
        this.users.put(user.id(), user);
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(this.users.get(id));
    }
}