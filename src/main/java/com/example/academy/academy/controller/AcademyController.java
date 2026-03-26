package com.example.academy.academy.controller;

import com.example.academy.academy.dto.request.CreateAcademyRequest;
import com.example.academy.academy.dto.response.AcademyResponse;
import com.example.academy.academy.service.AcademyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academies")
@Tag(name = "Academy Management", description = "Academy management APIs")
public class AcademyController {
    
    private final AcademyService academyService;
    private static final Logger log = LoggerFactory.getLogger(AcademyController.class);
    
    public AcademyController(AcademyService academyService) {
        this.academyService = academyService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_CREATE')")
    @Operation(summary = "Create a new academy")
    public ResponseEntity<AcademyResponse> createAcademy(@Valid @RequestBody CreateAcademyRequest request) {
        log.info("Creating new academy with code: {}", request.getCode());
        
        // Extract admin ID from authentication (in real implementation, you'd get this from user service)
        Long adminId = 1L; // Default admin ID for now
        
        AcademyResponse response = academyService.createAcademy(request, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Get academy by ID")
    public ResponseEntity<AcademyResponse> getAcademyById(@PathVariable Long id) {
        return academyService.getAcademyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Get academy by code")
    public ResponseEntity<AcademyResponse> getAcademyByCode(@PathVariable String code) {
        return academyService.getAcademyByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Get academy by name")
    public ResponseEntity<AcademyResponse> getAcademyByName(@PathVariable String name) {
        return academyService.getAcademyByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Get all academies with pagination")
    public ResponseEntity<Page<AcademyResponse>> getAllAcademies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<AcademyResponse> academies = academyService.getAllAcademies(pageable);
        return ResponseEntity.ok(academies);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Get academies by status")
    public ResponseEntity<List<AcademyResponse>> getAcademiesByStatus(@PathVariable String status) {
        List<AcademyResponse> academies = academyService.getAcademiesByStatus(status);
        return ResponseEntity.ok(academies);
    }
    
    @GetMapping("/city/{city}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Get active academies by city")
    public ResponseEntity<List<AcademyResponse>> getActiveAcademiesByCity(@PathVariable String city) {
        List<AcademyResponse> academies = academyService.getActiveAcademiesByCity(city);
        return ResponseEntity.ok(academies);
    }
    
    @GetMapping("/state/{state}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Get active academies by state")
    public ResponseEntity<List<AcademyResponse>> getActiveAcademiesByState(@PathVariable String state) {
        List<AcademyResponse> academies = academyService.getActiveAcademiesByState(state);
        return ResponseEntity.ok(academies);
    }
    
    @GetMapping("/country/{country}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Get active academies by country")
    public ResponseEntity<List<AcademyResponse>> getActiveAcademiesByCountry(@PathVariable String country) {
        List<AcademyResponse> academies = academyService.getActiveAcademiesByCountry(country);
        return ResponseEntity.ok(academies);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Search academies with filters")
    public ResponseEntity<Page<AcademyResponse>> searchAcademies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<AcademyResponse> academies = academyService.searchAcademies(name, city, state, status, pageable);
        return ResponseEntity.ok(academies);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_UPDATE')")
    @Operation(summary = "Update academy")
    public ResponseEntity<AcademyResponse> updateAcademy(
            @PathVariable Long id,
            @Valid @RequestBody CreateAcademyRequest request) {
        
        // Extract admin ID from authentication (in real implementation, you'd get this from user service)
        Long adminId = 1L; // Default admin ID for now
        
        AcademyResponse response = academyService.updateAcademy(id, request, adminId);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_DELETE')")
    @Operation(summary = "Delete academy")
    public ResponseEntity<Void> deleteAcademy(@PathVariable Long id) {
        academyService.deleteAcademy(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_UPDATE')")
    @Operation(summary = "Update academy status")
    public ResponseEntity<Void> updateAcademyStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        academyService.updateAcademyStatus(id, status);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/count/active")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Count active academies")
    public ResponseEntity<Long> countActiveAcademies() {
        long count = academyService.countActiveAcademies();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/active/city/{city}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Count active academies by city")
    public ResponseEntity<Long> countActiveAcademiesByCity(@PathVariable String city) {
        long count = academyService.countActiveAcademiesByCity(city);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/exists/code/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Check if academy code exists")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        boolean exists = academyService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/exists/name/{name}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ACADEMY_READ')")
    @Operation(summary = "Check if academy name exists")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        boolean exists = academyService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
}
