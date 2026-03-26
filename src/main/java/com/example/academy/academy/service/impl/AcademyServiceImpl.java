package com.example.academy.academy.service.impl;

import com.example.academy.academy.dto.request.CreateAcademyRequest;
import com.example.academy.academy.dto.response.AcademyResponse;
import com.example.academy.academy.entity.Academy;
import com.example.academy.academy.repository.AcademyRepository;
import com.example.academy.academy.service.AcademyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AcademyServiceImpl implements AcademyService {
    
    private static final Logger log = LoggerFactory.getLogger(AcademyServiceImpl.class);
    
    private final AcademyRepository academyRepository;
    
    public AcademyServiceImpl(AcademyRepository academyRepository) {
        this.academyRepository = academyRepository;
    }
    
    @Override
    public AcademyResponse createAcademy(CreateAcademyRequest request, Long createdBy) {
        log.info("Creating academy with code: {} and name: {}", request.getCode(), request.getName());
        
        if (academyRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Academy code already exists: " + request.getCode());
        }
        
        if (academyRepository.existsByName(request.getName())) {
            throw new RuntimeException("Academy name already exists: " + request.getName());
        }
        
        Academy academy = new Academy();
        academy.setName(request.getName());
        academy.setCode(request.getCode());
        academy.setDescription(request.getDescription());
        academy.setAddress(request.getAddress());
        academy.setCity(request.getCity());
        academy.setState(request.getState());
        academy.setCountry(request.getCountry());
        academy.setPostalCode(request.getPostalCode());
        academy.setPhoneNumber(request.getPhoneNumber());
        academy.setEmail(request.getEmail());
        academy.setWebsite(request.getWebsite());
        academy.setStatus(request.getStatus());
        academy.setCreatedBy(createdBy);
        
        Academy savedAcademy = academyRepository.save(academy);
        log.info("Academy created successfully with ID: {}", savedAcademy.getId());
        
        return convertToResponse(savedAcademy);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<AcademyResponse> getAcademyById(Long id) {
        return academyRepository.findById(id)
                .map(this::convertToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<AcademyResponse> getAcademyByCode(String code) {
        return academyRepository.findByCode(code)
                .map(this::convertToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<AcademyResponse> getAcademyByName(String name) {
        return academyRepository.findByName(name)
                .map(this::convertToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AcademyResponse> getAllAcademies(Pageable pageable) {
        return academyRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AcademyResponse> getAcademiesByStatus(String status) {
        return academyRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AcademyResponse> getActiveAcademiesByCity(String city) {
        return academyRepository.findByCityAndActive(city).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AcademyResponse> getActiveAcademiesByState(String state) {
        return academyRepository.findByStateAndActive(state).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AcademyResponse> getActiveAcademiesByCountry(String country) {
        return academyRepository.findByCountryAndActive(country).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AcademyResponse> searchAcademies(String name, String city, String state, String status, Pageable pageable) {
        return academyRepository.searchAcademies(name, city, state, status, pageable)
                .map(this::convertToResponse);
    }
    
    @Override
    public AcademyResponse updateAcademy(Long id, CreateAcademyRequest request, Long updatedBy) {
        log.info("Updating academy with ID: {}", id);
        
        Academy academy = academyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Academy not found with ID: " + id));
        
        // Check if name is being changed and if new name already exists
        if (!academy.getName().equals(request.getName()) && 
            academyRepository.existsByName(request.getName())) {
            throw new RuntimeException("Academy name already exists: " + request.getName());
        }
        
        // Check if code is being changed and if new code already exists
        if (!academy.getCode().equals(request.getCode()) && 
            academyRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Academy code already exists: " + request.getCode());
        }
        
        academy.setName(request.getName());
        academy.setCode(request.getCode());
        academy.setDescription(request.getDescription());
        academy.setAddress(request.getAddress());
        academy.setCity(request.getCity());
        academy.setState(request.getState());
        academy.setCountry(request.getCountry());
        academy.setPostalCode(request.getPostalCode());
        academy.setPhoneNumber(request.getPhoneNumber());
        academy.setEmail(request.getEmail());
        academy.setWebsite(request.getWebsite());
        academy.setStatus(request.getStatus());
        academy.setUpdatedBy(updatedBy);
        
        Academy savedAcademy = academyRepository.save(academy);
        log.info("Academy updated successfully with ID: {}", savedAcademy.getId());
        
        return convertToResponse(savedAcademy);
    }
    
    @Override
    public void deleteAcademy(Long id) {
        log.info("Deleting academy with ID: {}", id);
        
        Academy academy = academyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Academy not found with ID: " + id));
        
        academyRepository.delete(academy);
        log.info("Academy deleted successfully with ID: {}", id);
    }
    
    @Override
    public void updateAcademyStatus(Long id, String status) {
        log.info("Updating academy status for ID: {} to {}", id, status);
        
        Academy academy = academyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Academy not found with ID: " + id));
        
        academy.setStatus(status);
        academyRepository.save(academy);
        log.info("Academy status updated successfully for ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return academyRepository.existsByCode(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return academyRepository.existsByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveAcademies() {
        return academyRepository.countActiveAcademies();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveAcademiesByCity(String city) {
        return academyRepository.countActiveAcademiesByCity(city);
    }
    
    private AcademyResponse convertToResponse(Academy academy) {
        AcademyResponse response = new AcademyResponse();
        response.setId(academy.getId());
        response.setName(academy.getName());
        response.setCode(academy.getCode());
        response.setDescription(academy.getDescription());
        response.setAddress(academy.getAddress());
        response.setCity(academy.getCity());
        response.setState(academy.getState());
        response.setCountry(academy.getCountry());
        response.setPostalCode(academy.getPostalCode());
        response.setPhoneNumber(academy.getPhoneNumber());
        response.setEmail(academy.getEmail());
        response.setWebsite(academy.getWebsite());
        response.setEstablishedDate(academy.getEstablishedDate());
        response.setStatus(academy.getStatus());
        response.setCreatedAt(academy.getCreatedAt());
        response.setUpdatedAt(academy.getUpdatedAt());
        response.setCreatedBy(academy.getCreatedBy());
        response.setUpdatedBy(academy.getUpdatedBy());
        
        // Set batch summary information
        response.setTotalBatches(academy.getBatches().size());
        response.setActiveBatches((int) academy.getBatches().stream()
                .filter(batch -> "ACTIVE".equals(batch.getStatus()))
                .count());
        
        return response;
    }
}
