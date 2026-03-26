package com.example.academy.academy.service.impl;

import com.example.academy.academy.dto.request.CreateBatchRequest;
import com.example.academy.academy.dto.response.BatchResponse;
import com.example.academy.academy.entity.Academy;
import com.example.academy.academy.entity.Batch;
import com.example.academy.academy.repository.AcademyRepository;
import com.example.academy.academy.repository.BatchRepository;
import com.example.academy.academy.service.BatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BatchServiceImpl implements BatchService {
    
    private static final Logger log = LoggerFactory.getLogger(BatchServiceImpl.class);
    
    private final BatchRepository batchRepository;
    private final AcademyRepository academyRepository;
    
    public BatchServiceImpl(BatchRepository batchRepository, AcademyRepository academyRepository) {
        this.batchRepository = batchRepository;
        this.academyRepository = academyRepository;
    }
    
    @Override
    public BatchResponse createBatch(CreateBatchRequest request, Long createdBy) {
        log.info("Creating batch with code: {} for academy: {}", request.getBatchCode(), request.getAcademyId());
        
        if (batchRepository.existsByBatchCode(request.getBatchCode())) {
            throw new RuntimeException("Batch code already exists: " + request.getBatchCode());
        }
        
        Academy academy = academyRepository.findById(request.getAcademyId())
                .orElseThrow(() -> new RuntimeException("Academy not found with ID: " + request.getAcademyId()));
        
        Batch batch = new Batch();
        batch.setName(request.getName());
        batch.setBatchCode(request.getBatchCode());
        batch.setDescription(request.getDescription());
        batch.setAcademy(academy);
        batch.setCourseId(request.getCourseId());
        batch.setCourseName(request.getCourseName());
        batch.setMaxCapacity(request.getMaxCapacity());
        batch.setCurrentStrength(0); // Initially no students
        batch.setStartDate(request.getStartDate());
        batch.setEndDate(request.getEndDate());
        batch.setStatus(request.getStatus());
        batch.setBatchType(request.getBatchType());
        batch.setTiming(request.getTiming());
        batch.setDurationMonths(request.getDurationMonths());
        batch.setFees(request.getFees());
        batch.setCreatedBy(createdBy);
        
        Batch savedBatch = batchRepository.save(batch);
        log.info("Batch created successfully with ID: {}", savedBatch.getId());
        
        return convertToResponse(savedBatch);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<BatchResponse> getBatchById(Long id) {
        return batchRepository.findById(id)
                .map(this::convertToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<BatchResponse> getBatchByBatchCode(String batchCode) {
        return batchRepository.findByBatchCode(batchCode)
                .map(this::convertToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BatchResponse> getAllBatches(Pageable pageable) {
        return batchRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BatchResponse> getBatchesByAcademyId(Long academyId) {
        return batchRepository.findByAcademyId(academyId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BatchResponse> getBatchesByAcademyIdAndStatus(Long academyId, String status) {
        return batchRepository.findByAcademyIdAndStatus(academyId, status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BatchResponse> getBatchesByCourseId(Long courseId) {
        return batchRepository.findByCourseId(courseId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BatchResponse> getBatchesByStatus(String status) {
        return batchRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BatchResponse> searchBatches(Long academyId, String name, String status, Long courseId, Pageable pageable) {
        return batchRepository.searchBatches(academyId, name, status, courseId, pageable)
                .map(this::convertToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BatchResponse> getActiveBatchesByAcademy(Long academyId) {
        return batchRepository.findActiveBatchesByAcademy(academyId, LocalDateTime.now()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BatchResponse> getUpcomingBatchesByAcademy(Long academyId) {
        return batchRepository.findUpcomingBatchesByAcademy(academyId, LocalDateTime.now()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BatchResponse> getCompletedBatchesByAcademy(Long academyId) {
        return batchRepository.findCompletedBatchesByAcademy(academyId, LocalDateTime.now()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BatchResponse> getActiveBatchesByAcademyAndCourse(Long academyId, Long courseId) {
        return batchRepository.findActiveBatchesByAcademyAndCourse(academyId, courseId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public BatchResponse updateBatch(Long id, CreateBatchRequest request, Long updatedBy) {
        log.info("Updating batch with ID: {}", id);
        
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with ID: " + id));
        
        // Check if batch code is being changed and if new code already exists
        if (!batch.getBatchCode().equals(request.getBatchCode()) && 
            batchRepository.existsByBatchCode(request.getBatchCode())) {
            throw new RuntimeException("Batch code already exists: " + request.getBatchCode());
        }
        
        // Check if academy is being changed and if new academy exists
        if (!batch.getAcademy().getId().equals(request.getAcademyId())) {
            Academy academy = academyRepository.findById(request.getAcademyId())
                    .orElseThrow(() -> new RuntimeException("Academy not found with ID: " + request.getAcademyId()));
            batch.setAcademy(academy);
        }
        
        batch.setName(request.getName());
        batch.setBatchCode(request.getBatchCode());
        batch.setDescription(request.getDescription());
        batch.setCourseId(request.getCourseId());
        batch.setCourseName(request.getCourseName());
        batch.setMaxCapacity(request.getMaxCapacity());
        batch.setStartDate(request.getStartDate());
        batch.setEndDate(request.getEndDate());
        batch.setStatus(request.getStatus());
        batch.setBatchType(request.getBatchType());
        batch.setTiming(request.getTiming());
        batch.setDurationMonths(request.getDurationMonths());
        batch.setFees(request.getFees());
        batch.setUpdatedBy(updatedBy);
        
        Batch savedBatch = batchRepository.save(batch);
        log.info("Batch updated successfully with ID: {}", savedBatch.getId());
        
        return convertToResponse(savedBatch);
    }
    
    @Override
    public void deleteBatch(Long id) {
        log.info("Deleting batch with ID: {}", id);
        
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with ID: " + id));
        
        batchRepository.delete(batch);
        log.info("Batch deleted successfully with ID: {}", id);
    }
    
    @Override
    public void updateBatchStatus(Long id, String status) {
        log.info("Updating batch status for ID: {} to {}", id, status);
        
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found with ID: " + id));
        
        batch.setStatus(status);
        batchRepository.save(batch);
        log.info("Batch status updated successfully for ID: {}", id);
    }
    
    @Override
    public void incrementBatchStrength(Long batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found with ID: " + batchId));
        
        if (batch.getMaxCapacity() != null && batch.getCurrentStrength() >= batch.getMaxCapacity()) {
            throw new RuntimeException("Batch is already at maximum capacity");
        }
        
        batch.setCurrentStrength(batch.getCurrentStrength() + 1);
        batchRepository.save(batch);
    }
    
    @Override
    public void decrementBatchStrength(Long batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found with ID: " + batchId));
        
        if (batch.getCurrentStrength() <= 0) {
            throw new RuntimeException("Batch strength cannot be negative");
        }
        
        batch.setCurrentStrength(batch.getCurrentStrength() - 1);
        batchRepository.save(batch);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByBatchCode(String batchCode) {
        return batchRepository.existsByBatchCode(batchCode);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveBatchesByAcademy(Long academyId) {
        return batchRepository.countActiveBatchesByAcademy(academyId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countBatchesWithAvailableSeats(Long academyId) {
        return batchRepository.countBatchesWithAvailableSeats(academyId);
    }
    
    private BatchResponse convertToResponse(Batch batch) {
        BatchResponse response = new BatchResponse();
        response.setId(batch.getId());
        response.setName(batch.getName());
        response.setBatchCode(batch.getBatchCode());
        response.setDescription(batch.getDescription());
        response.setAcademyId(batch.getAcademy().getId());
        response.setAcademyName(batch.getAcademy().getName());
        response.setCourseId(batch.getCourseId());
        response.setCourseName(batch.getCourseName());
        response.setMaxCapacity(batch.getMaxCapacity());
        response.setCurrentStrength(batch.getCurrentStrength());
        
        // Calculate available seats
        if (batch.getMaxCapacity() != null) {
            response.setAvailableSeats(batch.getMaxCapacity() - batch.getCurrentStrength());
        }
        
        response.setStartDate(batch.getStartDate());
        response.setEndDate(batch.getEndDate());
        response.setStatus(batch.getStatus());
        response.setBatchType(batch.getBatchType());
        response.setTiming(batch.getTiming());
        response.setDurationMonths(batch.getDurationMonths());
        response.setFees(batch.getFees());
        response.setCreatedAt(batch.getCreatedAt());
        response.setUpdatedAt(batch.getUpdatedAt());
        response.setCreatedBy(batch.getCreatedBy());
        response.setUpdatedBy(batch.getUpdatedBy());
        
        return response;
    }
}
