package es.upm.miw.devops.rest;

import es.upm.miw.devops.code.UserActive;
import es.upm.miw.devops.code.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserResource {

    public static final String USERS = "/user";
    public static final String USERS_ALL = "/users";

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(UserResource.USERS_ALL)
    public List<UserDto> readAll() {
        return this.userService.find(null, null, null).stream().map(UserDto::of).toList();
    }

    @GetMapping(UserResource.USERS + "/search")
    public List<UserDto> search(@RequestParam(required = false) String firstName,
                                @RequestParam(required = false) String familyName,
                                @RequestParam(required = false) Boolean billable) {
        return this.userService.find(firstName, familyName, billable).stream().map(UserDto::of).toList();
    }

    @PatchMapping(UserResource.USERS)
    public List<UserDto> updateActive(@RequestBody List<UserActive> activeList) {
        return this.userService.updateActive(activeList).stream().map(UserDto::of).toList();
    }

    @GetMapping(UserResource.USERS + "/{id}")
    public UserDto read(@PathVariable String id) {
        return UserDto.of(this.userService.read(id));
    }

    @DeleteMapping(UserResource.USERS + "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(UserResource.USERS + "/{id}/active")
    public UserDto updateActive(@PathVariable String id) {
        return UserDto.of(this.userService.updateActive(id));
    }

    @PutMapping(UserResource.USERS + "/{id}")
    public UserDto update(@PathVariable String id, @RequestBody UserDto userDto) {
        return UserDto.of(this.userService.update(id, userDto.toUser()));
    }
}