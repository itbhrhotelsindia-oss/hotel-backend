// src/main/java/com/example/hotelbackend/service/HomePageService.java
package com.example.hotelbackend.service;

import com.example.hotelbackend.dto.HomePageData;

public interface HomePageService {
    HomePageData getHomePageData();
    HomePageData updateHomePageData(HomePageData homePageData);
    HomePageData getHomePageDataWithInactive();
}
