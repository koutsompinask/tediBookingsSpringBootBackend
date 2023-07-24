package com.project.tedi.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.tedi.model.Accomodation;
import com.project.tedi.model.Photo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhotoService {

    @Value("${photo.upload.directory}") // Set the value in application.properties
    private String uploadDirectory;

    public String savePhoto(MultipartFile file) {
        try {
            String filename = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = Paths.get(uploadDirectory, filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save photo: " + e.getMessage());
        }
    }

    public Resource getPhoto(String filename) {
        try {
            Path filePath = Paths.get(uploadDirectory, filename);
            UrlResource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Photo not found");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error while retrieving photo: " + e.getMessage());
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        return UUID.randomUUID().toString() + fileExtension;
    }

    public void deletePhoto(String filename) {
        try {
            Path filePath = Paths.get(uploadDirectory, filename);
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete photo: " + e.getMessage());
        }
    }
}

