package com.mohdiop.deme_api.entity;

import com.mohdiop.deme_api.dto.response.ArgumentResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "arguments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Argument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long argumentId;

    @Column(nullable = false)
    private String argumentBody;

    @Column(nullable = false)
    private LocalDateTime argumentDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "proof_id")
    private Proof proof;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "report_id")
    private Report relatedReport;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "author_id")
    private User author;

    public ArgumentResponse toResponse() {
        if (proof != null) {
            return new ArgumentResponse(
                    argumentId,
                    author.getUserId(),
                    argumentBody,
                    argumentDate,
                    proof.toResponse()
            );
        } else {
            return new ArgumentResponse(
                    argumentId,
                    author.getUserId(),
                    argumentBody,
                    argumentDate,
                    null
            );
        }
    }
}
