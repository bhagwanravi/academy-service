package com.example.academy.academy.repository;

import com.example.academy.academy.entity.Academy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademyRepository extends JpaRepository<Academy, Long> {
    
    Optional<Academy> findByCode(String code);
    
    Optional<Academy> findByName(String name);
    
    boolean existsByCode(String code);
    
    boolean existsByName(String name);
    
    List<Academy> findByStatus(String status);
    
    @Query("SELECT a FROM Academy a WHERE a.city = :city AND a.status = 'ACTIVE'")
    List<Academy> findByCityAndActive(@Param("city") String city);
    
    @Query("SELECT a FROM Academy a WHERE a.state = :state AND a.status = 'ACTIVE'")
    List<Academy> findByStateAndActive(@Param("state") String state);
    
    @Query("SELECT a FROM Academy a WHERE a.country = :country AND a.status = 'ACTIVE'")
    List<Academy> findByCountryAndActive(@Param("country") String country);
    
    @Query("SELECT a FROM Academy a WHERE " +
           "(:name IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:city IS NULL OR LOWER(a.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:state IS NULL OR LOWER(a.state) LIKE LOWER(CONCAT('%', :state, '%'))) AND " +
           "(:status IS NULL OR a.status = :status)")
    Page<Academy> searchAcademies(@Param("name") String name,
                                  @Param("city") String city,
                                  @Param("state") String state,
                                  @Param("status") String status,
                                  Pageable pageable);
    
    @Query("SELECT COUNT(a) FROM Academy a WHERE a.status = 'ACTIVE'")
    long countActiveAcademies();
    
    @Query("SELECT COUNT(a) FROM Academy a WHERE a.city = :city AND a.status = 'ACTIVE'")
    long countActiveAcademiesByCity(@Param("city") String city);
}
