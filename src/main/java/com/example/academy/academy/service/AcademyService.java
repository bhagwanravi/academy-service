package com.example.academy.academy.service;

import com.example.academy.academy.dto.request.CreateAcademyRequest;
import com.example.academy.academy.dto.response.AcademyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AcademyService {
    
    AcademyResponse createAcademy(CreateAcademyRequest request, Long createdBy);
    
    Optional<AcademyResponse> getAcademyById(Long id);
    
    Optional<AcademyResponse> getAcademyByCode(String code);
    
    Optional<AcademyResponse> getAcademyByName(String name);
    
    Page<AcademyResponse> getAllAcademies(Pageable pageable);
    
    List<AcademyResponse> getAcademiesByStatus(String status);
    
    List<AcademyResponse> getActiveAcademiesByCity(String city);
    
    List<AcademyResponse> getActiveAcademiesByState(String state);
    
    List<AcademyResponse> getActiveAcademiesByCountry(String country);
    
    Page<AcademyResponse> searchAcademies(String name, String city, String state, String status, Pageable pageable);
    
    AcademyResponse updateAcademy(Long id, CreateAcademyRequest request, Long updatedBy);
    
    void deleteAcademy(Long id);
    
    void updateAcademyStatus(Long id, String status);
    
    boolean existsByCode(String code);
    
    boolean existsByName(String name);
    
    long countActiveAcademies();
    
    long countActiveAcademiesByCity(String city);
}
