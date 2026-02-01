package com.example.hotelbackend.controller;

import com.example.hotelbackend.service.ImageKitService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/imagekit")
public class ImageKitController {

    private final ImageKitService imageKitService;

    public ImageKitController(ImageKitService imageKitService) {
        this.imageKitService = imageKitService;
    }

    /* =====================================================
       SINGLE FILE UPLOAD
       POST /api/imagekit/upload/{folder}/{subfolder}
       ===================================================== */
    @PostMapping(
            value = "/upload/{folder}/{subfolder}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadSingle(
            @PathVariable String folder,
            @PathVariable String subfolder,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        String path = folder + "/" + subfolder;
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Map<String, Object> result =
                imageKitService.upload(file, path, fileName);

        return ResponseEntity.ok(result);
    }

    /* =====================================================
       BULK FILE UPLOAD
       POST /api/imagekit/upload/bulk/{folder}/{subfolder}
       ===================================================== */
    @PostMapping(
            value = "/upload/bulk/{folder}/{subfolder}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadBulk(
            @PathVariable String folder,
            @PathVariable String subfolder,
            @RequestParam("files") MultipartFile[] files
    ) throws Exception {

        String path = folder + "/" + subfolder;
        List<Map<String, Object>> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName =
                    UUID.randomUUID() + "_" + file.getOriginalFilename();

            uploadedFiles.add(
                    imageKitService.upload(file, path, fileName)
            );
        }

        return ResponseEntity.ok(uploadedFiles);
    }

    /* =====================================================
       LIST FILES IN FOLDER
       GET /api/imagekit/list/{folder}/{subfolder}
       ===================================================== */
    @GetMapping("/list/{folder}/{subfolder}")
    public ResponseEntity<?> listFiles(
            @PathVariable String folder,
            @PathVariable String subfolder
    ) {
        return ResponseEntity.ok(
                imageKitService.list(folder + "/" + subfolder)
        );
    }

    /* =====================================================
       DELETE FILE
       DELETE /api/imagekit/delete/{fileId}
       ===================================================== */
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<?> deleteFile(
            @PathVariable String fileId
    ) {
        imageKitService.delete(fileId);
        return ResponseEntity.ok("Deleted: " + fileId);
    }
}
