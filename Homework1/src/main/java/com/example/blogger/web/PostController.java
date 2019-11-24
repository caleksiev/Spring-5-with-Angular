package com.example.blogger.web;

import com.example.blogger.exception.InvalidEntityException;
import com.example.blogger.model.Post;
import com.example.blogger.model.UserPrincipal;
import com.example.blogger.services.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.naming.NoPermissionException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostServiceImpl postService;

    @GetMapping
    public List<Post> getPosts() {
        return postService.findAll();
    }

    @GetMapping("{id}")
    public Post getPostById(@PathVariable("id") String postId) {
        return postService.findById(postId);
    }

    @PostMapping
    public ResponseEntity<Post> addPost( @RequestBody Post post, @AuthenticationPrincipal UserPrincipal userPrincipal,
                                        BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throwError(bindingResult);
        }
        Post addedPost = postService.add(post, userPrincipal);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}").build(addedPost.getId()))
                .body(addedPost);
    }

    @PutMapping("{id}")
    public Post updatePost(@PathVariable String id,@RequestBody Post post, @AuthenticationPrincipal UserPrincipal userPrincipal,
                           BindingResult bindingResult) throws NoPermissionException {
        if (bindingResult.hasFieldErrors()) {
            throwError(bindingResult);
        }

        if (!id.equals(post.getId())) {
            throw new InvalidEntityException(
                    String.format("Entity ID='%s' is different from URL resource ID='%s'", post.getId(), id));
        }
        return postService.update(post, userPrincipal);
    }

    @DeleteMapping("{id}")
    public Post removePost(@PathVariable String id, @AuthenticationPrincipal UserPrincipal userPrincipal) throws NoPermissionException {
        return postService.remove(id, userPrincipal);
    }

    private void throwError(BindingResult bindingResult) {
        String message = bindingResult.getFieldErrors().stream()
                .map(err -> String.format("Invalid '%s' -> '%s': %s\n",
                        err.getField(), err.getRejectedValue(), err.getDefaultMessage()))
                .reduce("", (acc, errStr) -> acc + errStr);
        throw new InvalidEntityException(message);
    }
}
