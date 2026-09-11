package es.upm.miw.devops.rest;

import es.upm.miw.devops.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {

    public static final String USERS = "/user";

    @GetMapping("/{id}")
    public User read(@PathVariable String id) {
        return new User(id, "name-" + id, "familyName-" + id);
    }
}