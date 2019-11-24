package com.example.blogger.web;

import com.example.blogger.exception.InvalidEntityException;
import com.example.blogger.model.User;
import com.example.blogger.model.UserPrincipal;
import com.example.blogger.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable("id") String userId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userService.findById(userId, userPrincipal);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throwError(bindingResult);
        }
        User addedUser = userService.add(user);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}").build(addedUser.getId()))
                .body(addedUser);
    }

    @PutMapping("{id}")
    public User updateUser(@PathVariable String id, @Valid @RequestBody User user, @AuthenticationPrincipal UserPrincipal userPrincipal,
                           BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throwError(bindingResult);
        }

        if (!id.equals(user.getId())) {
            throw new InvalidEntityException(
                    String.format("Entity ID='%s' is different from URL resource ID='%s'", user.getId(), id));
        }
        return userService.update(user, userPrincipal);
    }

    @DeleteMapping("{id}")
    public User removeUser(@PathVariable String id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userService.remove(id, userPrincipal);
    }

    private void throwError(BindingResult bindingResult) {
        String message = bindingResult.getFieldErrors().stream()
                .map(err -> String.format("Invalid '%s' -> '%s': %s\n",
                        err.getField(), err.getRejectedValue(), err.getDefaultMessage()))
                .reduce("", (acc, errStr) -> acc + errStr);
        throw new InvalidEntityException(message);
    }
}
