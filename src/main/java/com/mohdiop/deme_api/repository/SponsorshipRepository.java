package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Sponsorship;
import com.mohdiop.deme_api.entity.enumeration.SponsorshipState;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SponsorshipRepository extends JpaRepository<Sponsorship, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Sponsorship s " +
            "SET s.sponsorshipState = com.mohdiop.deme_api.entity.enumeration.SponsorshipState.FINISHED " +
            "WHERE (s.sponsorshipState = com.mohdiop.deme_api.entity.enumeration.SponsorshipState.IN_PROGRESS" +
            " OR s.sponsorshipState = com.mohdiop.deme_api.entity.enumeration.SponsorshipState.FROM_TRANSFER)" +
            "AND s.sponsorshipEndAt < CURRENT_TIMESTAMP")
    void endSponsorships();

    @Modifying
    @Transactional
    @Query("DELETE FROM Sponsorship s " +
            "WHERE s.sponsorshipState = com.mohdiop.deme_api.entity.enumeration.SponsorshipState.PENDING")
    void deletePendingSponsorships();

    Optional<Sponsorship> findByStudentUserIdAndSponsorshipState(Long studentId, SponsorshipState sponsorshipState);

    List<Sponsorship> findBySponsorUserId(Long organizationId);

    Optional<Sponsorship> findByStudentUserId(Long studentId);
}
