package com.example.academy.academy.service;

import com.example.academy.academy.dto.request.CreateBatchRequest;
import com.example.academy.academy.dto.response.BatchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BatchService {
    
    BatchResponse createBatch(CreateBatchRequest request, Long createdBy);
    
    Optional<BatchResponse> getBatchById(Long id);
    
    Optional<BatchResponse> getBatchByBatchCode(String batchCode);
    
    Page<BatchResponse> getAllBatches(Pageable pageable);
    
    List<BatchResponse> getBatchesByAcademyId(Long academyId);
    
    List<BatchResponse> getBatchesByAcademyIdAndStatus(Long academyId, String status);
    
    List<BatchResponse> getBatchesByCourseId(Long courseId);
    
    List<BatchResponse> getBatchesByStatus(String status);
    
    Page<BatchResponse> searchBatches(Long academyId, String name, String status, Long courseId, Pageable pageable);
    
    List<BatchResponse> getActiveBatchesByAcademy(Long academyId);
    
    List<BatchResponse> getUpcomingBatchesByAcademy(Long academyId);
    
    List<BatchResponse> getCompletedBatchesByAcademy(Long academyId);
    
    List<BatchResponse> getActiveBatchesByAcademyAndCourse(Long academyId, Long courseId);
    
    BatchResponse updateBatch(Long id, CreateBatchRequest request, Long updatedBy);
    
    void deleteBatch(Long id);
    
    void updateBatchStatus(Long id, String status);
    
    void incrementBatchStrength(Long batchId);
    
    void decrementBatchStrength(Long batchId);
    
    boolean existsByBatchCode(String batchCode);
    
    long countActiveBatchesByAcademy(Long academyId);
    
    long countBatchesWithAvailableSeats(Long academyId);
}
