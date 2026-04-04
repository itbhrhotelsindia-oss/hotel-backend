package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.HomePageData;
import com.example.hotelbackend.service.HomePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomePageService homePageService;

    @GetMapping
    public HomePageData getHomePage() {
        return homePageService.getHomePageData();
    }

    @PutMapping
    public HomePageData updateHomePage(@RequestBody HomePageData homePageData) {
        return homePageService.updateHomePageData(homePageData);
    }

    // ⭐ NEW Admin API (active + inactive)
    @GetMapping("/all")
    public HomePageData getHomePageAll() {

        return homePageService
                .getHomePageDataWithInactive();
    }

}