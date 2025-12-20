package com.example.hotelbackend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/cloudimages")
public class CloudImageController {

    @Autowired
    private Cloudinary cloudinary;

    // --------------------------------------------------
    // UPLOAD SINGLE IMAGE
    // --------------------------------------------------
    @PostMapping("/upload/{folder}/{subfolder}")
    public ResponseEntity<?> uploadImage(
            @PathVariable String folder,
            @PathVariable String subfolder,
            @RequestParam("file") MultipartFile file) throws Exception {

        String cloudFolder = folder + "/" + subfolder;

        int nextIndex = getNextIndex(cloudFolder, subfolder);
        String publicId = subfolder + "_" + nextIndex;

        Map uploadResult;

        if (file.getContentType() != null && file.getContentType().startsWith("video")) {
            // ✅ LARGE VIDEO UPLOAD
            uploadResult = cloudinary.uploader().uploadLarge(
                    file.getInputStream(),
                    ObjectUtils.asMap(
                            "folder", cloudFolder,
                            "public_id", publicId,
                            "resource_type", "video",
                            "chunk_size", 6_000_000
                    )
            );
        } else {
            // ✅ IMAGE UPLOAD
            uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", cloudFolder,
                            "public_id", publicId,
                            "resource_type", "image"
                    )
            );
        }

        return ResponseEntity.ok(Map.of(
                "publicId", uploadResult.get("public_id"),
                "url", uploadResult.get("secure_url")
        ));
    }


    // --------------------------------------------------
    // LIST IMAGES
    // --------------------------------------------------
    @GetMapping("/list/{folder}/{subfolder}")
    public ResponseEntity<?> listImages(
            @PathVariable String folder,
            @PathVariable String subfolder) throws Exception {

        String cloudFolder = folder + "/" + subfolder;

        Map result = cloudinary.api().resources(
                ObjectUtils.asMap(
                        "type", "upload",
                        "prefix", cloudFolder + "/",
                        "max_results", 100
                )
        );

        List<Map<String, Object>> images = new ArrayList<>();

        List<Map> resources = (List<Map>) result.get("resources");
        for (Map r : resources) {
            images.add(Map.of(
                    "publicId", r.get("public_id"),
                    "url", r.get("secure_url")
            ));
        }

        return ResponseEntity.ok(images);
    }

    // --------------------------------------------------
    // DELETE IMAGE (by index like JimCorbett_3)
    // --------------------------------------------------
    @DeleteMapping("/delete/{folder}/{subfolder}/{index}")
    public ResponseEntity<?> deleteImage(
            @PathVariable String folder,
            @PathVariable String subfolder,
            @PathVariable int index) throws Exception {

        String publicId = folder + "/" + subfolder + "/" + subfolder + "_" + index;

        cloudinary.uploader().destroy(
                publicId,
                ObjectUtils.asMap("resource_type", "image")
        );

        return ResponseEntity.ok("Deleted: " + publicId);
    }

    // --------------------------------------------------
    // HELPER: GET NEXT INDEX
    // --------------------------------------------------
    private int getNextIndex(String folder, String subfolder) throws Exception {

        Map result = cloudinary.api().resources(
                ObjectUtils.asMap(
                        "type", "upload",
                        "prefix", folder + "/" + subfolder + "_",
                        "max_results", 100
                )
        );

        int max = 0;
        List<Map> resources = (List<Map>) result.get("resources");

        for (Map r : resources) {
            String id = (String) r.get("public_id");
            String name = id.substring(id.lastIndexOf("_") + 1);
            try {
                max = Math.max(max, Integer.parseInt(name));
            } catch (Exception ignored) {}
        }
        return max + 1;
    }
}
