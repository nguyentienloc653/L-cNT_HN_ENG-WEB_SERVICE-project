package com.example.datsancaulong.service.Imp;

import com.example.datsancaulong.exception.ImageExtentionInvalidExeption;
import com.example.datsancaulong.exception.InternalServerException;
import com.example.datsancaulong.service.UploadImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadImageServiceImp implements UploadImageService {
    private static final Logger logger = LoggerFactory.getLogger(UploadImageServiceImp.class);

    // provide a safe default so bean creation won't fail if property is missing
    @Value("${imageUploadDir:./uploads/images}")
    private String imageUploadDir;

    @Override
    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null || !fileName.contains(".")) {
            throw new ImageExtentionInvalidExeption("Ảnh không đúng định dạng");
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();

        if (!fileExtension.equals(".jpg")
                && !fileExtension.equals(".jpeg")
                && !fileExtension.equals(".png")) {
            throw new ImageExtentionInvalidExeption("Ảnh không đúng định dạng");
        }

        try {
            Path dirPath = Paths.get(imageUploadDir);
            Files.createDirectories(dirPath);

            String newFileName = UUID.randomUUID() + "_" + fileName;
            Path dest = dirPath.resolve(newFileName);

            file.transferTo(dest.toFile());
            return "/images/" + newFileName;
        } catch (IOException e) {
            logger.error("Failed to store uploaded image", e);
            throw new InternalServerException("Upload ảnh thất bại", e);
        }
    }
}
