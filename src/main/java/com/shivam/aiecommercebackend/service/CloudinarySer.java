package com.shivam.aiecommercebackend.service;

import com.cloudinary.Cloudinary;
import com.shivam.aiecommercebackend.dto.CloudinaryUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinarySer {

    @Autowired
    private Cloudinary cloudinary;
    @PreAuthorize("isAuthenticated()")
    public CloudinaryUploadResult uploadImage(MultipartFile file){
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "folder", "products",
                            "resource_type", "image"
                    )
            );

            // 🔥 IMPORTANT URLs
            String secureUrl = uploadResult.get("secure_url").toString();
            String publicId  = uploadResult.get("public_id").toString();

            System.out.println("Cloudinary URL: " + secureUrl);
            System.out.println("Public ID: " + publicId);

            return new CloudinaryUploadResult(secureUrl,publicId); // ✅ Save this in DB

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteImage(String publicId) {
        if (publicId == null || publicId.isBlank()) return;

        try {
            cloudinary.uploader().destroy(
                    publicId,
                    Map.of("resource_type", "image")
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image from Cloudinary", e);
        }
    }
}
