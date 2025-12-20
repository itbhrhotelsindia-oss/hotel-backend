package com.example.hotelbackend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryImageService {

    @Autowired
    private Cloudinary cloudinary;

    // ---------------- Upload ----------------
    public Map upload(String folder, String subfolder, MultipartFile file, int index)
            throws IOException {

        String cloudFolder = folder + "/" + subfolder;
        String publicId = subfolder + "_" + index;

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", cloudFolder,
                        "public_id", publicId,
                        "overwrite", true,
                        "resource_type", "auto" // image + video
                )
        );

        return Map.of(
                "publicId", publicId,
                "url", uploadResult.get("secure_url").toString()
        );
    }

    // ---------------- Delete ----------------
    public void delete(String folder, String subfolder, int index) throws IOException {
        String publicId = folder + "/" + subfolder + "/" + subfolder + "_" + index;
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}

