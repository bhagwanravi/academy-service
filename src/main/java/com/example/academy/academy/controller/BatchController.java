package com.example.academy.academy.controller;

import com.example.academy.academy.dto.request.CreateBatchRequest;
import com.example.academy.academy.dto.response.BatchResponse;
import com.example.academy.academy.service.BatchService;
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
@RequestMapping("/api/batches")
@Tag(name = "Batch Management", description = "Batch management APIs")
public class BatchController {
    
    private final BatchService batchService;
    private static final Logger log = LoggerFactory.getLogger(BatchController.class);
    
    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_CREATE')")
    @Operation(summary = "Create a new batch")
    public ResponseEntity<BatchResponse> createBatch(@Valid @RequestBody CreateBatchRequest request) {
        log.info("Creating new batch with code: {}", request.getBatchCode());
        
        // Extract admin ID from authentication (in real implementation, you'd get this from user service)
        Long adminId = 1L; // Default admin ID for now
        
        BatchResponse response = batchService.createBatch(request, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Get batch by ID")
    public ResponseEntity<BatchResponse> getBatchById(@PathVariable Long id) {
        return batchService.getBatchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{batchCode}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Get batch by batch code")
    public ResponseEntity<BatchResponse> getBatchByBatchCode(@PathVariable String batchCode) {
        return batchService.getBatchByBatchCode(batchCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Get all batches with pagination")
    public ResponseEntity<Page<BatchResponse>> getAllBatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<BatchResponse> batches = batchService.getAllBatches(pageable);
        return ResponseEntity.ok(batches);
    }
    
    @GetMapping("/academy/{academyId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Get batches by academy ID")
    public ResponseEntity<List<BatchResponse>> getBatchesByAcademyId(@PathVariable Long academyId) {
        List<BatchResponse> batches = batchService.getBatchesByAcademyId(academyId);
        return ResponseEntity.ok(batches);
    }
    
    @GetMapping("/academy/{academyId}/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Get batches by academy ID and status")
    public ResponseEntity<List<BatchResponse>> getBatchesByAcademyIdAndStatus(
            @PathVariable Long academyId,
            @PathVariable String status) {
        List<BatchResponse> batches = batchService.getBatchesByAcademyIdAndStatus(academyId, status);
        return ResponseEntity.ok(batches);
    }
    
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Get batches by course ID")
    public ResponseEntity<List<BatchResponse>> getBatchesByCourseId(@PathVariable Long courseId) {
        List<BatchResponse> batches = batchService.getBatchesByCourseId(courseId);
        return ResponseEntity.ok(batches);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Get batches by status")
    public ResponseEntity<List<BatchResponse>> getBatchesByStatus(@PathVariable String status) {
        List<BatchResponse> batches = batchService.getBatchesByStatus(status);
        return ResponseEntity.ok(batches);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Search batches with filters")
    public ResponseEntity<Page<BatchResponse>> searchBatches(
            @RequestParam Long academyId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<BatchResponse> batches = batchService.searchBatches(academyId, name, status, courseId, pageable);
        return ResponseEntity.ok(batches);
    }
    
    @GetMapping("/academy/{academyId}/active")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Get active batches by academy")
    public ResponseEntity<List<BatchResponse>> getActiveBatchesByAcademy(@PathVariable Long academyId) {
        List<BatchResponse> batches = batchService.getActiveBatchesByAcademy(academyId);
        return ResponseEntity.ok(batches);
    }
    
    @GetMapping("/academy/{academyId}/upcoming")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Get upcoming batches by academy")
    public ResponseEntity<List<BatchResponse>> getUpcomingBatchesByAcademy(@PathVariable Long academyId) {
        List<BatchResponse> batches = batchService.getUpcomingBatchesByAcademy(academyId);
        return ResponseEntity.ok(batches);
    }
    
    @GetMapping("/academy/{academyId}/completed")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Get completed batches by academy")
    public ResponseEntity<List<BatchResponse>> getCompletedBatchesByAcademy(@PathVariable Long academyId) {
        List<BatchResponse> batches = batchService.getCompletedBatchesByAcademy(academyId);
        return ResponseEntity.ok(batches);
    }
    
    @GetMapping("/academy/{academyId}/course/{courseId}/active")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Get active batches by academy and course")
    public ResponseEntity<List<BatchResponse>> getActiveBatchesByAcademyAndCourse(
            @PathVariable Long academyId,
            @PathVariable Long courseId) {
        List<BatchResponse> batches = batchService.getActiveBatchesByAcademyAndCourse(academyId, courseId);
        return ResponseEntity.ok(batches);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_UPDATE')")
    @Operation(summary = "Update batch")
    public ResponseEntity<BatchResponse> updateBatch(
            @PathVariable Long id,
            @Valid @RequestBody CreateBatchRequest request) {
        
        // Extract admin ID from authentication (in real implementation, you'd get this from user service)
        Long adminId = 1L; // Default admin ID for now
        
        BatchResponse response = batchService.updateBatch(id, request, adminId);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_DELETE')")
    @Operation(summary = "Delete batch")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_UPDATE')")
    @Operation(summary = "Update batch status")
    public ResponseEntity<Void> updateBatchStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        batchService.updateBatchStatus(id, status);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/increment-strength")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_UPDATE')")
    @Operation(summary = "Increment batch current strength")
    public ResponseEntity<Void> incrementBatchStrength(@PathVariable Long id) {
        batchService.incrementBatchStrength(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/decrement-strength")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_UPDATE')")
    @Operation(summary = "Decrement batch current strength")
    public ResponseEntity<Void> decrementBatchStrength(@PathVariable Long id) {
        batchService.decrementBatchStrength(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/count/active/academy/{academyId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Count active batches by academy")
    public ResponseEntity<Long> countActiveBatchesByAcademy(@PathVariable Long academyId) {
        long count = batchService.countActiveBatchesByAcademy(academyId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/available-seats/academy/{academyId}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Count batches with available seats by academy")
    public ResponseEntity<Long> countBatchesWithAvailableSeats(@PathVariable Long academyId) {
        long count = batchService.countBatchesWithAvailableSeats(academyId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/exists/code/{batchCode}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('BATCH_READ')")
    @Operation(summary = "Check if batch code exists")
    public ResponseEntity<Boolean> existsByBatchCode(@PathVariable String batchCode) {
        boolean exists = batchService.existsByBatchCode(batchCode);
        return ResponseEntity.ok(exists);
    }
}
