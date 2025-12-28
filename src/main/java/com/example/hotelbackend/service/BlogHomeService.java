package com.example.hotelbackend.service;

import com.example.hotelbackend.model.BlogHomeContent;

public interface BlogHomeService {

    BlogHomeContent getBlogHome();

    BlogHomeContent updateBlogHome(BlogHomeContent content);

    // Blog List Section
    BlogHomeContent addBlogToList(BlogHomeContent.BlogItem item);
    BlogHomeContent deleteBlogFromList(String blogId);

    // Popular Posts Section
    BlogHomeContent addPopularPost(BlogHomeContent.BlogItem item);
    BlogHomeContent deletePopularPost(String postId);
}

