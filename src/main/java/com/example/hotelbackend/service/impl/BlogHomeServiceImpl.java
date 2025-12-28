package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.model.BlogHomeContent;
import com.example.hotelbackend.repository.BlogHomeRepository;
import com.example.hotelbackend.service.BlogHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BlogHomeServiceImpl implements BlogHomeService {

    private final BlogHomeRepository repository;

    @Override
    public BlogHomeContent getBlogHome() {
        return repository.findFirstBy()
                .orElseGet(BlogHomeContent::new);
    }

    @Override
    public BlogHomeContent updateBlogHome(BlogHomeContent content) {
        BlogHomeContent existing = repository.findFirstBy()
                .orElseGet(BlogHomeContent::new);

        content.setId(existing.getId()); // keep single document
        return repository.save(content);
    }

    /* =======================
       BLOG LIST SECTION
       ======================= */

    @Override
    public BlogHomeContent addBlogToList(BlogHomeContent.BlogItem item) {
        BlogHomeContent content = repository.findFirstBy()
                .orElseThrow(() -> new RuntimeException("Blog home not found"));

        if (content.getBlogListSection() == null) {
            content.setBlogListSection(new BlogHomeContent.BlogListSection());
        }

        if (content.getBlogListSection().getBlogsList() == null) {
            content.getBlogListSection().setBlogsList(new ArrayList<>());
        }

        content.getBlogListSection().getBlogsList().add(item);
        return repository.save(content);
    }

    @Override
    public BlogHomeContent deleteBlogFromList(String blogId) {
        BlogHomeContent content = repository.findFirstBy()
                .orElseThrow(() -> new RuntimeException("Blog home not found"));

        if (content.getBlogListSection() != null &&
                content.getBlogListSection().getBlogsList() != null) {

            content.getBlogListSection()
                    .getBlogsList()
                    .removeIf(blog -> blogId.equals(blog.getId()));
        }

        return repository.save(content);
    }

    /* =======================
       POPULAR POSTS
       ======================= */

    @Override
    public BlogHomeContent addPopularPost(BlogHomeContent.BlogItem item) {
        BlogHomeContent content = repository.findFirstBy()
                .orElseThrow(() -> new RuntimeException("Blog home not found"));

        if (content.getPopularPostListSection() == null) {
            content.setPopularPostListSection(
                    new BlogHomeContent.PopularPostListSection()
            );
        }

        if (content.getPopularPostListSection().getPostsList() == null) {
            content.getPopularPostListSection().setPostsList(new ArrayList<>());
        }

        content.getPopularPostListSection().getPostsList().add(item);
        return repository.save(content);
    }

    @Override
    public BlogHomeContent deletePopularPost(String postId) {
        BlogHomeContent content = repository.findFirstBy()
                .orElseThrow(() -> new RuntimeException("Blog home not found"));

        if (content.getPopularPostListSection() != null &&
                content.getPopularPostListSection().getPostsList() != null) {

            content.getPopularPostListSection()
                    .getPostsList()
                    .removeIf(post -> postId.equals(post.getId()));
        }

        return repository.save(content);
    }
}

