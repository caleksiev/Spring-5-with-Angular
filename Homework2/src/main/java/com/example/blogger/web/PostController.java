package com.example.blogger.web;

import com.example.blogger.model.Post;
import com.example.blogger.model.UserPrincipal;
import com.example.blogger.services.PostServiceImpl;
import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2RTFDTM;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/posts")
@Slf4j
public class PostController {
    @Autowired
    private PostServiceImpl postService;

    @GetMapping("/actual-sortDate")
    public String getLastPostSortDate(Model model) {
        List<Post> posts = postService.findAll();
        posts.sort((o1, o2) -> {
            if (o1.getPublicationDate().isEqual(o2.getPublicationDate()))
                return 0;
            return o2.getPublicationDate().isBefore(o1.getPublicationDate()) ? -1 : 1;
        });
        model.addAttribute("posts", posts.stream()
                .limit(15)
                .collect(Collectors.toList()));
        model.addAttribute("path", "posts/actual-sortDate");
        return "last-posts";
    }

    @GetMapping("/actual-sortStatus")
    public String getLastPostsSortStatus(Model model) {
        List<Post> posts = postService.findAll();
        posts.sort((o1, o2) -> Boolean.compare(o1.isActive(),o2.isActive()));
        model.addAttribute("posts", posts.stream()
                .limit(15)
                .collect(Collectors.toList()));
        model.addAttribute("path", "posts/actual-sortStatus");
        return "last-posts";
    }

    @GetMapping
    public String getPosts(Model model) {
        model.addAttribute("posts", postService.findAllByRole());
        model.addAttribute("path", "posts");
        return "posts";
    }

    @PostMapping(params = "edit")
    public String editPost(@RequestParam("edit") String postId,
                           UriComponentsBuilder uriBuilder) {
        if (postId != null) {
            URI uri = uriBuilder.path("/posts/post-form")
                    .query("mode=edit&&postId={articleId}")
                    .buildAndExpand(postId).toUri();
            return "redirect:" + uri.toString();
        }
        return "redirect:/posts";
    }


    @PostMapping(params = "delete")
    public String deleteArticle(@RequestParam("delete") String postId, UserPrincipal userPrincipal) throws NoPermissionException {
        if (postId != null) {
            postService.remove(postId, userPrincipal);
        }
        return "redirect:/posts";
    }


    @GetMapping("/post-form")
    public String getUserForm(@ModelAttribute("post") Post post, ModelMap model,
                              @RequestParam(value = "mode", required = false) String mode,
                              @RequestParam(value = "postId", required = false) String postId) throws NoPermissionException {
        model.addAttribute("path", "posts/post-form");
        String title = "lbl.add.post";
        if (mode != null && mode.equals("edit") && postId != null) {
            Post postToEdit = postService.findById(postId);
            model.put("post", postToEdit);
            title = "lbl.edit.post";
        }
        model.addAttribute("title", title);
        return "post-form";
    }

    @PostMapping("/post-form")
    public String addPost(@Valid @ModelAttribute("post") Post post,
                          BindingResult errors,
                          @RequestParam("file") MultipartFile file,
                          Model model, UserPrincipal userPrincipal,
                          RedirectAttributes redirectAttributes) throws NoPermissionException {

        String title = post.getId() == null ? "lbl.add.post" : "lbl.edit.post";
        model.addAttribute("title", title);
        model.addAttribute("alreadyExists", null);

        if (errors.hasErrors()) {
            List<String> errorMessages = errors.getAllErrors().stream().map(err -> {
                ConstraintViolation cv = err.unwrap(ConstraintViolation.class);
                return String.format("Error in '%s' - invalid value: '%s'", cv.getPropertyPath(), cv.getInvalidValue());
            }).collect(Collectors.toList());
            model.addAttribute("myErrors", errorMessages);
            model.addAttribute("fileError", null);
            return "post-form";
        } else {
            log.info("POST User: " + post);
            if (!file.isEmpty() && file.getOriginalFilename().length() > 0) {
                if (Pattern.matches("[\\w\\s]+\\.(jpg|png)", file.getOriginalFilename())) {
                    handleMultipartFile(file);
                    post.setImageUrl(file.getOriginalFilename());
                } else {
                    model.addAttribute("myErrors", null);
                    model.addAttribute("fileError", "Submit picture [.jpg, .png]");
                    return "post-form";
                }
            }
            if (post.getId() == null) {
                log.info("ADD New User: " + post);
                postService.add(post, userPrincipal);
            } else {
                log.info("UPDATE User: " + post);
                postService.update(post, userPrincipal);
            }
            return "redirect:/posts";
        }
    }

//    @GetMapping("{id}")
//    public Post getPostById(@PathVariable("id") String postId) {
//        return postService.findById(postId);
//    }


    //    @PutMapping("{id}")
//    public Post updatePost(@PathVariable String id, @RequestBody Post post, @AuthenticationPrincipal UserPrincipal userPrincipal,
//                           BindingResult bindingResult) throws NoPermissionException {
//        if (bindingResult.hasFieldErrors()) {
//            throwError(bindingResult);
//        }
//
//        if (!id.equals(post.getId())) {
//            throw new InvalidEntityException(
//                    String.format("Entity ID='%s' is different from URL resource ID='%s'", post.getId(), id));
//        }
//        return postService.update(post, userPrincipal);
//    }
//
//    @DeleteMapping("{id}")
//    public Post removePost(@PathVariable String id, @AuthenticationPrincipal UserPrincipal userPrincipal) throws NoPermissionException {
//        return postService.remove(id, userPrincipal);
//    }
//
    public String renderer(String text) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(text);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }


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
            File currentDir = new File("tmp1");
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
