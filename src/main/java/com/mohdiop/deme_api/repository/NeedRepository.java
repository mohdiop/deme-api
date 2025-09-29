package com.mohdiop.deme_api.repository;

import com.mohdiop.deme_api.entity.Need;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NeedRepository extends JpaRepository<Need, Long> {
    @Modifying
    @Query("UPDATE Need n SET n.needState = com.mohdiop.deme_api.entity.enumeration.NeedState.EXPIRED " +
            "WHERE n.needExpiresAt < CURRENT_TIMESTAMP AND n.needState = com.mohdiop.deme_api.entity.enumeration.NeedState.UNSATISFIED")
    void expireNeeds();

    List<Need> findByStudentUserId(Long studentId);
}
