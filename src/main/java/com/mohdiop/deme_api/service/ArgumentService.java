package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.creation.CreateArgumentRequest;
import com.mohdiop.deme_api.dto.response.ArgumentResponse;
import com.mohdiop.deme_api.entity.Argument;
import com.mohdiop.deme_api.entity.Report;
import com.mohdiop.deme_api.entity.User;
import com.mohdiop.deme_api.entity.enumeration.ProofType;
import com.mohdiop.deme_api.entity.enumeration.ReportState;
import com.mohdiop.deme_api.repository.ArgumentRepository;
import com.mohdiop.deme_api.repository.ProofRepository;
import com.mohdiop.deme_api.repository.ReportRepository;
import com.mohdiop.deme_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class ArgumentService {

    private final ArgumentRepository argumentRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final ProofRepository proofRepository;
    private final ProofService proofService;

    public ArgumentService(ArgumentRepository argumentRepository, UserRepository userRepository, ReportRepository reportRepository, ProofRepository proofRepository, ProofService proofService) {
        this.argumentRepository = argumentRepository;
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.proofRepository = proofRepository;
        this.proofService = proofService;
    }

    public ArgumentResponse createArgument(
            Long authorId,
            Long reportId,
            CreateArgumentRequest createArgumentRequest
    ) throws BadRequestException {
        User user = userRepository.findById(authorId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Utilisateur introuvable.")
                );
        Report report = reportRepository.findById(reportId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Signalement introuvable.")
                );
        if (report.getReportState() != ReportState.OPEN) {
            throw new BadRequestException("Signalement fermé, impossible de l'argumenter.");
        }
        Argument argument = createArgumentRequest.toArgument();
        if (createArgumentRequest.proof() != null) {
            argument.setProof(
                    proofService.uploadProof(
                            createArgumentRequest.proof(),
                            ProofType.ARGUMENT
                    )
            );
        } else if (createArgumentRequest.proofId() != null) {
            argument.setProof(
                    proofRepository.findById(createArgumentRequest.proofId())
                            .orElseThrow(
                                    () -> new EntityNotFoundException("Preuve introuvable.")
                            )
            );
        }
        argument.setAuthor(user);
        argument.setRelatedReport(report);
        return argumentRepository.save(argument).toResponse();
    }
}
