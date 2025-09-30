package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.entity.enumeration.FileType;
import com.mohdiop.deme_api.entity.enumeration.ProofType;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@Service
public class UploadService {

    private final String userHome = System.getProperty("user.home");
    private final String desktopPath = userHome + File.separator + "Desktop";

    public String uploadFile(MultipartFile file, String fileName, @Nullable ProofType proofType, FileType fileType, Boolean isProof) {
        boolean isCreatedDirectory;
        if (isProof && proofType != null) {
            switch (proofType) {
                case ACTIVITY -> {
                    File directory = new File(desktopPath + "/proofs/activities");
                    if (!directory.exists()) {
                        isCreatedDirectory = directory.mkdirs();
                    } else {
                        isCreatedDirectory = true;
                    }
                }
                case ARGUMENT -> {
                    File directory = new File(desktopPath + "/proofs/arguments");
                    if (!directory.exists()) {
                        isCreatedDirectory = directory.mkdirs();
                    } else {
                        isCreatedDirectory = true;
                    }
                }
                case EXPENSE -> {
                    File directory = new File(desktopPath + "/proofs/expenses");
                    if (!directory.exists()) {
                        isCreatedDirectory = directory.mkdirs();
                    } else {
                        isCreatedDirectory = true;
                    }
                }
                default -> isCreatedDirectory = false;
            }
        } else {
            File directory = new File(desktopPath + "/report-cards");
            if (!directory.exists()) {
                isCreatedDirectory = directory.mkdirs();
            } else {
                isCreatedDirectory = true;
            }
        }
        if (isCreatedDirectory) {
            String filePath = getFilePath(fileName, proofType, fileType, isProof);
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(file.getBytes());
                return filePath;
            } catch (Exception e) {
                throw new RuntimeException("Un problème est survenu de notre côté, veuillez réessayer plus tard.");
            }
        } else {
            return null;
        }
    }

    private String getFilePath(String fileName, @Nullable ProofType proofType, FileType fileType, Boolean isProof) {
        String filePath;
        String dotExtension = getDotExtensionType(fileType);
        if (isProof && proofType != null) {
            switch (proofType) {
                case ACTIVITY ->
                        filePath = desktopPath + File.separator + "proofs" + File.separator + "activities" + File.separator + fileName + dotExtension;
                case ARGUMENT ->
                        filePath = desktopPath + File.separator + "proofs" + File.separator + "arguments" + File.separator + fileName + dotExtension;
                case EXPENSE ->
                        filePath = desktopPath + File.separator + "proofs" + File.separator + "expenses" + File.separator + fileName + dotExtension;
                default ->
                        throw new RuntimeException("Un problème est survenu de notre côté, veuillez réessayer plus tard.");
            }
        } else {
            filePath = desktopPath + File.separator + "report-cards" + File.separator + fileName + dotExtension;
        }
        return filePath;
    }

    private String getDotExtensionType(FileType fileType) {
        switch (fileType) {
            case JPG -> {
                return ".jpg";
            }
            case PNG -> {
                return ".png";
            }
            case PDF -> {
                return ".pdf";
            }
        }
        throw new RuntimeException("Un problème est survenu de notre côté, veuillez réessayer plus tard.");
    }
}
