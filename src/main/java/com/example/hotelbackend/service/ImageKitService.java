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
import java.util.List;
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
       DELETE FILE BY URL
       =============================== */
    public void deleteByUrl(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("ik.imagekit.io")) return;

        try {
            // Extract filename (strip query params)
            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            if (fileName.contains("?")) {
                fileName = fileName.substring(0, fileName.indexOf('?'));
            }

            String auth = Base64.getEncoder()
                    .encodeToString((config.getPrivateKey() + ":").getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + auth);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            String searchUrl = "https://api.imagekit.io/v1/files?searchQuery=name%3D%22"
                    + fileName + "%22";

            ResponseEntity<List> response = restTemplate.exchange(
                    searchUrl, HttpMethod.GET, request, List.class);

            List<Map<String, Object>> files = response.getBody();
            if (files != null && !files.isEmpty()) {
                String fileId = (String) files.get(0).get("fileId");
                if (fileId != null) delete(fileId);
            }
        } catch (Exception e) {
            // Log but don't fail the whole cleanup if one image delete fails
            System.err.println("Failed to delete ImageKit file: " + imageUrl + " — " + e.getMessage());
        }
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
