package es.upm.miw.devops;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User read(String id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }

    public void delete(String id) {
        this.userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
        this.userRepository.deleteById(id);
    }

    public User updateActive(String id) {
        User user = this.read(id);
        user.setActive(!user.getActive());
        return this.userRepository.save(user);
    }

    public List<User> find(String firstName, String familyName, Boolean billable) {
        return this.userRepository.findAll().stream()
                .filter(user -> !hasContent(firstName) || firstName.equals(user.getFirstName()))
                .filter(user -> !hasContent(familyName) || familyName.equals(user.getFamilyName()))
                .filter(user -> billable == null || billable.equals(user.isBillable()))
                .toList();
    }

    private static boolean hasContent(String value) {
        return value != null && !value.isBlank();
    }
}