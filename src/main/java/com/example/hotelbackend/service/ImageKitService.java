package com.example.hotelbackend.service;

import com.example.hotelbackend.config.ImageKitConfig;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;

@Service
public class ImageKitService {

    private final ImageKitConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    public ImageKitService(ImageKitConfig config) {
        this.config = config;
    }

    /* ===============================
       UPLOAD FILE TO IMAGEKIT
       =============================== */
    public Map upload(MultipartFile file, String folderPath, String fileName)
            throws Exception {

        String uploadUrl = "https://upload.imagekit.io/api/v1/files/upload";

        // 🔑 AUTH HEADER (Base64)
        String auth = Base64.getEncoder()
                .encodeToString((config.getPrivateKey() + ":").getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Basic " + auth);

        // ✅ MULTIPART BODY (IMPORTANT)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        body.add("fileName", fileName);
        body.add("folder", "/" + folderPath);

        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(uploadUrl, request, Map.class);

        return response.getBody();
    }

    /* ===============================
       LIST FILES
       =============================== */
    public Object list(String folder) {

        String url =
                "https://api.imagekit.io/v1/files?path=/" + folder;

        String auth = Base64.getEncoder()
                .encodeToString((config.getPrivateKey() + ":").getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + auth);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                Object.class
        ).getBody();
    }

    /* ===============================
       DELETE FILE
       =============================== */
    public void delete(String fileId) {

        String url =
                "https://api.imagekit.io/v1/files/" + fileId;

        String auth = Base64.getEncoder()
                .encodeToString((config.getPrivateKey() + ":").getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + auth);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                request,
                Void.class
        );
    }
}
