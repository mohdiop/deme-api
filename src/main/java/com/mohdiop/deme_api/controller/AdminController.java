package com.mohdiop.deme_api.controller;

import com.mohdiop.deme_api.dto.request.creation.CreateOrganizationRequest;
import com.mohdiop.deme_api.dto.response.*;
import com.mohdiop.deme_api.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    private final SponsorService sponsorService;
    private final OrganizationService organizationService;
    private final StudentService studentService;
    private final UserService userService;
    private final NeedService needService;
    private final TutorService tutorService;
    private final SponsorshipService sponsorshipService;
    private final PaymentService paymentService;
    private final ActivityService activityService;
    private final ExpenseService expenseService;

    public AdminController(SponsorService sponsorService, OrganizationService organizationService, StudentService studentService, UserService userService, NeedService needService, TutorService tutorService, SponsorshipService sponsorshipService, PaymentService paymentService, ActivityService activityService, ExpenseService expenseService) {
        this.sponsorService = sponsorService;
        this.organizationService = organizationService;
        this.studentService = studentService;
        this.userService = userService;
        this.needService = needService;
        this.tutorService = tutorService;
        this.sponsorshipService = sponsorshipService;
        this.paymentService = paymentService;
        this.activityService = activityService;
        this.expenseService = expenseService;
    }

    @PostMapping("/register/organizations")
    public ResponseEntity<OrganizationResponse> createSchool(
            @Valid @RequestBody CreateOrganizationRequest createOrganizationRequest
    ) {
        return new ResponseEntity<>(
                organizationService.createOrganization(createOrganizationRequest),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/sponsors")
    public ResponseEntity<List<SponsorResponse>> getAllSponsors() {
        return ResponseEntity.ok(sponsorService.getAllSponsors());
    }

    @GetMapping("/organizations")
    public ResponseEntity<List<OrganizationResponse>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/users")
    public ResponseEntity<List<Record>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/needs")
    public ResponseEntity<List<NeedResponse>> getAllNeeds() {
        return ResponseEntity.ok(needService.getAllNeeds());
    }

    @GetMapping("/tutors")
    public ResponseEntity<List<TutorResponse>> getAllTutors() {
        return ResponseEntity.ok(tutorService.getAllTutors());
    }

    @GetMapping("/sponsorships")
    public ResponseEntity<List<SponsorshipResponse>> getAllSponsorships() {
        return ResponseEntity.ok(sponsorshipService.getAllSponsorships());
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/activities")
    public ResponseEntity<List<ActivityResponse>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }
}
