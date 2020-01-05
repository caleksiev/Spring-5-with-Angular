package com.example.blogger.web;

import com.example.blogger.exception.InvalidEntityException;
import com.example.blogger.model.User;
import com.example.blogger.model.UserPrincipal;
import com.example.blogger.services.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.naming.NoPermissionException;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@Slf4j
public class UserController {

    private static final String UPLOADS_DIR = "tmp";

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public String getUser(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("path", "users");
        return "users";
    }

    @PostMapping(params = "edit")
    public String editPost(@RequestParam("edit") String userId,
                           UriComponentsBuilder uriBuilder) {
        if (userId != null) {
            URI uri = uriBuilder.path("/users/user-form")
                    .query("mode=edit&&userId={userId}")
                    .buildAndExpand(userId).toUri();
            return "redirect:" + uri.toString();
        }
        return "redirect:/users";
    }

    @PostMapping(params = "delete")
    public String deleteArticle(@RequestParam("delete") String userId) throws NoPermissionException {
        if (userId != null) {
            userService.changeStatus(userId);
        }
        return "redirect:/users";
    }

//    @GetMapping("{id}")
//    public User getUserById(@PathVariable("id") String userId, @AuthenticationPrincipal UserPrincipal userPrincipal) throws NoPermissionException {
//        return userService.findById(userId, userPrincipal);
//    }

    @GetMapping("/user-form")
    public String getUserForm(@ModelAttribute("user") User user, ModelMap model,
                              UserPrincipal userPrincipal,
                              @RequestParam(value = "mode", required = false) String mode,
                              @RequestParam(value = "userId", required = false) String userId) throws NoPermissionException {
        model.addAttribute("path", "users/user-form");
        String title = "lbl.add.user";
        if (mode != null && mode.equals("edit") && userId != null) {
            User userToEdit = userService.findById(userId, userPrincipal);
            model.put("user", userToEdit);
            title = "lbl.edit.user";
        }
        model.addAttribute("title", title);
        return "user-form";
    }

    @PostMapping("/user-form")
    public String addUser(@Valid @ModelAttribute("user") User user,
                          BindingResult errors,
                          @RequestParam("file") MultipartFile file,
                          Model model, @AuthenticationPrincipal UserPrincipal userPrincipal,
                          RedirectAttributes redirectAttributes) throws NoPermissionException {

        String title = user.getId() == null ? "lbl.add.user" : "lbl.edit.user";
        model.addAttribute("title", title);
        model.addAttribute("alreadyExists", null);

        if (errors.hasErrors()) {
            List<String> errorMessages = errors.getAllErrors().stream().map(err -> {
                ConstraintViolation cv = err.unwrap(ConstraintViolation.class);
                return String.format("Error in '%s' - invalid value: '%s'", cv.getPropertyPath(), cv.getInvalidValue());
            }).collect(Collectors.toList());
            model.addAttribute("myErrors", errorMessages);
            model.addAttribute("fileError", null);
            return "user-form";
        } else {
            log.info("POST User: " + user);
            if (!file.isEmpty() && file.getOriginalFilename().length() > 0) {
                if (Pattern.matches("[\\w\\s]+\\.(jpg|png)", file.getOriginalFilename())) {
                    handleMultipartFile(file);
                    user.setUserProfilePhoto(file.getOriginalFilename());
                } else {
                    model.addAttribute("myErrors", null);
                    model.addAttribute("fileError", "Submit picture [.jpg, .png]");
                    return "user-form";
                }
            }
            if (user.getId() == null) {
                log.info("ADD New User: " + user);
                try {
                    userService.add(user);
                } catch (InvalidEntityException e) {
                    model.addAttribute("alreadyExists", e.getMessage());
                    return "user-form";
                }
            } else {
                log.info("UPDATE User: " + user);
                userService.update(user, userPrincipal);
            }
            redirectAttributes.addAttribute("success", "");

            if (title.equals("lbl.add.user")) {
                return "redirect:/login";
            } else {
                return "redirect:/users";
            }
        }
    }

//    @PutMapping("{id}")
//    public User updateUser(@PathVariable String id, @Valid @RequestBody User user, @AuthenticationPrincipal UserPrincipal userPrincipal,
//                           BindingResult bindingResult) throws NoPermissionException {
//        if (bindingResult.hasFieldErrors()) {
//            throwError(bindingResult);
//        }
//
//        if (!id.equals(user.getId())) {
//            throw new InvalidEntityException(
//                    String.format("Entity ID='%s' is different from URL resource ID='%s'", user.getId(), id));
//        }
//        return userService.update(user, userPrincipal);
//    }
//
//    @DeleteMapping("{id}")
//    public User removeUser(@PathVariable String id, @AuthenticationPrincipal UserPrincipal userPrincipal) throws NoPermissionException {
//        return userService.remove(id, userPrincipal);
//    }

//    private void throwError(BindingResult bindingResult) {
//        String message = bindingResult.getFieldErrors().stream()
//                .map(err -> String.format("Invalid '%s' -> '%s': %s\n",
//                        err.getField(), err.getRejectedValue(), err.getDefaultMessage()))
//                .reduce("", (acc, errStr) -> acc + errStr);
//        throw new InvalidEntityException(message);
//    }

    private void handleMultipartFile(MultipartFile file) {
        String name = file.getOriginalFilename();
        long size = file.getSize();
        log.info("File: " + name + ", Size: " + size);
        try {
            File currentDir = new File(UPLOADS_DIR);
            if (!currentDir.exists()) {
                currentDir.mkdirs();
            }
            String path = currentDir.getAbsolutePath() + "/" + file.getOriginalFilename();
            path = new File(path).getAbsolutePath();
            log.info(path);
            File f = new File(path);
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(f));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
