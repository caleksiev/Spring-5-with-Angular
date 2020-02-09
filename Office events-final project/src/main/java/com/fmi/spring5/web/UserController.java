package com.fmi.spring5.web;

import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.exceptions.InvalidArgumentException;
import com.fmi.spring5.exceptions.NoSuchEntityException;
import com.fmi.spring5.model.Dev;
import com.fmi.spring5.model.User;
import com.fmi.spring5.service.UserService;
import com.fmi.spring5.service.UserServiceImpl;
import org.graalvm.compiler.lir.LIRInstruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<Dev> registered(@Valid @RequestBody User user) throws EntityAlreadyExistsException {
        Dev addedUser = userService.addUser(user);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}").build(addedUser.getId()))
                .body(addedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> logged(@AuthenticationPrincipal Principal principal) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/me")
    ResponseEntity<User> getUserRole(@AuthenticationPrincipal Principal user) throws NoSuchEntityException {
        return ResponseEntity.ok().body(this.userService.getUser(user.getName()));
    }

    @PutMapping("/users/me")
    public ResponseEntity<User> updateMe(@Valid @RequestBody User user, @AuthenticationPrincipal Principal principal) throws NoSuchEntityException {
        if (!user.getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().body(userService.update(user));
    }

    @GetMapping("/users")
    @PostAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @PatchMapping("/users/{username}")
    @PostAuthorize("hasRole('ADMIN')")
    public ResponseEntity changeUserRole(@PathVariable String username, @RequestParam("role") String role)
            throws NoSuchEntityException {
        try {
            userService.changeUserRole(username, role);
        } catch (InvalidArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users")
    @PostAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) throws NoSuchEntityException {
        return ResponseEntity.ok().body(userService.update(user));
    }

}
