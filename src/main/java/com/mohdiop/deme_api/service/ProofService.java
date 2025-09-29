package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.response.ProofResponse;
import com.mohdiop.deme_api.entity.Proof;
import com.mohdiop.deme_api.entity.enumeration.FileType;
import com.mohdiop.deme_api.entity.enumeration.ProofType;
import com.mohdiop.deme_api.repository.ProofRepository;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProofService {

    private final ProofRepository proofRepository;

    private final UploadService uploadService;

    public ProofService(ProofRepository proofRepository, UploadService uploadService) {
        this.proofRepository = proofRepository;
        this.uploadService = uploadService;
    }

    public Proof uploadProof(MultipartFile proofFile, ProofType proofType) {
        Tika tika = new Tika();
        String detectedType;
        try {
            detectedType = tika.detect(proofFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Un problème est survenu de notre côté, veuillez réessayer plus tard.");
        }
        if (!("application/pdf".equals(detectedType) ||
                "image/jpeg".equals(detectedType) ||
                "image/png".equals(detectedType))) {
            throw new IllegalArgumentException("Type de fichier non supporté : " + detectedType);
        }
        FileType fileType;
        if ("application/pdf".equals(detectedType)) {
            fileType = FileType.PDF;
        } else if ("image/jpeg".equals(detectedType)) {
            fileType = FileType.JPG;
        } else {
            fileType = FileType.PNG;
        }
        String url = uploadService.uploadFile(
                proofFile,
                UUID.randomUUID().toString(),
                proofType,
                fileType
        );
        return proofRepository.save(
                Proof.builder()
                        .proofId(null)
                        .proofType(proofType)
                        .proofFileType(fileType)
                        .proofUrl(url)
                        .proofUploadedAt(LocalDateTime.now())
                        .build()
        );
    }
}
