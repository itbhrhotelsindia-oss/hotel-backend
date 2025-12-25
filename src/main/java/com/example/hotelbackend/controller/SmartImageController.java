package com.example.hotelbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api/images")
public class SmartImageController {

    // FINAL CORRECT PATH
    private final String BASE_DIR = "/Users/shivasingh/HotelUploads/";

    // ----------------------------------------------------------
    private Map<String, String> saveFile(String folder, String subfolder, MultipartFile file) throws IOException {

        String dirPath = BASE_DIR + folder + "/" + subfolder + "/";
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();

        File[] existing = dir.listFiles((d, name) -> name.startsWith(subfolder + "_"));

        int nextIndex = 1;
        if (existing != null) {
            for (File f : existing) {
                try {
                    String base = f.getName().replace(".jpg", "");
                    String[] parts = base.split("_");
                    int idx = Integer.parseInt(parts[1]);
                    nextIndex = Math.max(nextIndex, idx + 1);
                } catch (Exception ignored) {}
            }
        }

        String newFileName = subfolder + "_" + nextIndex + ".jpg";
        File saveFile = new File(dirPath + newFileName);
        file.transferTo(saveFile);

        String url = "/uploads/" + folder + "/" + subfolder + "/" + newFileName;

        return Map.of("filename", newFileName, "url", url);
    }

    // ----------------------------------------------------------
    @GetMapping("/list/{folder}/{subfolder}")
    public ResponseEntity<?> listImages(
            @PathVariable String folder,
            @PathVariable String subfolder) {

        String dirPath = BASE_DIR + folder + "/" + subfolder + "/";
        File dir = new File(dirPath);

        if (!dir.exists()) dir.mkdirs();

        File[] files = dir.listFiles((d, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
        List<String> list = new ArrayList<>();

        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));
            for (File f : files) list.add(f.getName());
        }

        return ResponseEntity.ok(list);
    }

    // ----------------------------------------------------------
    @PostMapping("/upload-single/{folder}/{subfolder}")
    public ResponseEntity<?> uploadSingle(
            @PathVariable String folder,
            @PathVariable String subfolder,
            @RequestParam("file") MultipartFile file) throws IOException {

        return ResponseEntity.ok(saveFile(folder, subfolder, file));
    }

    // ----------------------------------------------------------
    @PostMapping("/upload/{folder}/{subfolder}")
    public ResponseEntity<?> uploadMultiple(
            @PathVariable String folder,
            @PathVariable String subfolder,
            @RequestParam("files") MultipartFile[] files) throws IOException {

        List<Map<String, String>> uploaded = new ArrayList<>();
        for (MultipartFile f : files) uploaded.add(saveFile(folder, subfolder, f));

        return ResponseEntity.ok(uploaded);
    }

    // ----------------------------------------------------------
    @DeleteMapping("/delete/{folder}/{subfolder}/{index}")
    public ResponseEntity<?> deleteImage(
            @PathVariable String folder,
            @PathVariable String subfolder,
            @PathVariable int index) throws IOException {

        String dirPath = BASE_DIR + folder + "/" + subfolder + "/";
        File dir = new File(dirPath);

        if (!dir.exists()) return ResponseEntity.status(404).body("Folder not found");

        File remove = new File(dirPath + subfolder + "_" + index + ".jpg");
        if (!remove.exists()) return ResponseEntity.status(404).body("Image not found");

        remove.delete();

        File[] files = dir.listFiles((d, name) -> name.startsWith(subfolder + "_"));
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));

            int counter = 1;
            for (File f : files) {
                File newFile = new File(dirPath + subfolder + "_" + counter + ".jpg");
                Files.move(f.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                counter++;
            }
        }

        return ResponseEntity.ok("Deleted and reordered");
    }
}
