package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.service.ProofService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/proofs")
public class ProofController {

    private final ProofService proofService;

    public ProofController(ProofService proofService) {
        this.proofService = proofService;
    }
}
