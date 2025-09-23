package com.mohdiop.deme_api.service;

import com.mohdiop.deme_api.dto.request.CreateNeedRequest;
import com.mohdiop.deme_api.dto.response.NeedResponse;
import com.mohdiop.deme_api.entity.Need;
import com.mohdiop.deme_api.entity.Student;
import com.mohdiop.deme_api.repository.NeedRepository;
import com.mohdiop.deme_api.repository.SchoolRepository;
import com.mohdiop.deme_api.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
public class NeedService {

    private final NeedRepository needRepository;
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;

    public NeedService(NeedRepository needRepository, SchoolRepository schoolRepository, StudentRepository studentRepository) {
        this.needRepository = needRepository;
        this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
    }

    public NeedResponse createNeed(Long demanderId, Long studentId, CreateNeedRequest createNeedRequest) {
        schoolRepository.findById(demanderId)
                .orElseThrow(() -> new EntityNotFoundException("Ecole introuvable."));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Elève introuvable."));
        if (!demanderId.equals(student.getSchool().getUserId())) {
            throw new AccessDeniedException("Cet(te) élève n'appartient à cette école.");
        }
        Need needToStore = createNeedRequest.toStudentlessNeed();
        needToStore.setStudent(student);
        return needRepository.save(needToStore).toResponse();
    }
}
