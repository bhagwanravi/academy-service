package com.example.academy.academy.repository;

import com.example.academy.academy.entity.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    
    Optional<Batch> findByBatchCode(String batchCode);
    
    boolean existsByBatchCode(String batchCode);
    
    List<Batch> findByAcademyId(Long academyId);
    
    List<Batch> findByAcademyIdAndStatus(Long academyId, String status);
    
    List<Batch> findByCourseId(Long courseId);
    
    List<Batch> findByStatus(String status);
    
    @Query("SELECT b FROM Batch b WHERE b.academy.id = :academyId AND " +
           "(:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(:courseId IS NULL OR b.courseId = :courseId)")
    Page<Batch> searchBatches(@Param("academyId") Long academyId,
                             @Param("name") String name,
                             @Param("status") String status,
                             @Param("courseId") Long courseId,
                             Pageable pageable);
    
    @Query("SELECT b FROM Batch b WHERE b.academy.id = :academyId AND b.status = 'ACTIVE' AND " +
           "b.startDate <= :currentDate AND b.endDate >= :currentDate")
    List<Batch> findActiveBatchesByAcademy(@Param("academyId") Long academyId,
                                           @Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT b FROM Batch b WHERE b.academy.id = :academyId AND b.status = 'ACTIVE' AND " +
           "b.startDate > :currentDate")
    List<Batch> findUpcomingBatchesByAcademy(@Param("academyId") Long academyId,
                                            @Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT b FROM Batch b WHERE b.academy.id = :academyId AND b.status = 'ACTIVE' AND " +
           "b.endDate < :currentDate")
    List<Batch> findCompletedBatchesByAcademy(@Param("academyId") Long academyId,
                                              @Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT COUNT(b) FROM Batch b WHERE b.academy.id = :academyId AND b.status = 'ACTIVE'")
    long countActiveBatchesByAcademy(@Param("academyId") Long academyId);
    
    @Query("SELECT COUNT(b) FROM Batch b WHERE b.academy.id = :academyId AND b.status = 'ACTIVE' AND " +
           "b.currentStrength < b.maxCapacity")
    long countBatchesWithAvailableSeats(@Param("academyId") Long academyId);
    
    @Query("SELECT b FROM Batch b WHERE b.academy.id = :academyId AND b.courseId = :courseId AND b.status = 'ACTIVE'")
    List<Batch> findActiveBatchesByAcademyAndCourse(@Param("academyId") Long academyId,
                                                     @Param("courseId") Long courseId);
}
