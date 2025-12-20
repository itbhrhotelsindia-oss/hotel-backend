package com.example.hotelbackend.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name","djjy8drrx");
        config.put("api_key","649159827651774");
        config.put("api_secret","3k9st2lfhI4Cm2x0s85c6iaTWec");
        return new Cloudinary(config);
    }
}

