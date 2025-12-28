package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.BlogHomeContent;
import com.example.hotelbackend.service.BlogHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class BlogHomeController {

    private final BlogHomeService service;

    /* =======================
       GET BLOG HOME
       ======================= */
    @GetMapping
    public ResponseEntity<BlogHomeContent> getBlogHome() {
        return ResponseEntity.ok(service.getBlogHome());
    }

    /* =======================
       UPDATE FULL BLOG HOME
       ======================= */
    @PutMapping
    public ResponseEntity<BlogHomeContent> updateBlogHome(
            @RequestBody BlogHomeContent content) {
        return ResponseEntity.ok(service.updateBlogHome(content));
    }

    /* =======================
       BLOG LIST SECTION
       ======================= */

    @PostMapping("/blog-list")
    public ResponseEntity<BlogHomeContent> addBlog(
            @RequestBody BlogHomeContent.BlogItem item) {
        return ResponseEntity.ok(service.addBlogToList(item));
    }

    @DeleteMapping("/blog-list/{blogId}")
    public ResponseEntity<String> deleteBlog(@PathVariable String blogId) {
        service.deleteBlogFromList(blogId);
        return ResponseEntity.ok("Blog deleted successfully");
    }

    /* =======================
       POPULAR POSTS
       ======================= */

    @PostMapping("/popular-posts")
    public ResponseEntity<BlogHomeContent> addPopularPost(
            @RequestBody BlogHomeContent.BlogItem item) {
        return ResponseEntity.ok(service.addPopularPost(item));
    }

    @DeleteMapping("/popular-posts/{postId}")
    public ResponseEntity<String> deletePopularPost(
            @PathVariable String postId) {
        service.deletePopularPost(postId);
        return ResponseEntity.ok("Popular post deleted successfully");
    }
}

