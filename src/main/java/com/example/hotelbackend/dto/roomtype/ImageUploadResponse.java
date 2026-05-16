package com.example.hotelbackend.dto.roomtype;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageUploadResponse {
    private String url;
    private String fileName;
    private String fileId;
}
