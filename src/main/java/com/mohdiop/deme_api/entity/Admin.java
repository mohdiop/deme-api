package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.AdminResponse;
import com.mohdiop.deme_api.entity.enumeration.UserRole;
import com.mohdiop.deme_api.entity.enumeration.UserState;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "admins")
public class Admin extends User {

    @Builder
    public Admin(
            Long id,
            String phone,
            String email,
            String password,
            String pictureUrl,
            Set<UserRole> roles,
            UserState state
    ) {
        super(id, phone, email, password, pictureUrl, roles, LocalDateTime.now(), state);
    }

    public AdminResponse toResponse() {
        return new AdminResponse(
                super.getUserId(),
                super.getUserPhone(),
                super.getUserEmail(),
                super.getUserRoles(),
                super.getUserState(),
                super.getUserCreatedAt()
        );
    }
}
