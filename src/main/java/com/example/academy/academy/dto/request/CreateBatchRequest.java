package com.example.academy.academy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class CreateBatchRequest {
    
    @NotBlank(message = "Batch name is required")
    private String name;
    
    @NotBlank(message = "Batch code is required")
    @Pattern(regexp = "^[A-Z0-9]{3,15}$", message = "Batch code must be 3-15 uppercase letters and numbers")
    private String batchCode;
    
    private String description;
    
    @NotNull(message = "Academy ID is required")
    private Long academyId;
    
    private Long courseId;
    
    private String courseName;
    
    @Positive(message = "Max capacity must be positive")
    private Integer maxCapacity;
    
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    private String status = "ACTIVE";
    
    private String batchType;
    
    private String timing;
    
    private Integer durationMonths;
    
    private Double fees;
    
    // Default constructor
    public CreateBatchRequest() {}
    
    // All args constructor
    public CreateBatchRequest(String name, String batchCode, String description, Long academyId,
                            Long courseId, String courseName, Integer maxCapacity,
                            LocalDateTime startDate, LocalDateTime endDate, String status,
                            String batchType, String timing, Integer durationMonths, Double fees) {
        this.name = name;
        this.batchCode = batchCode;
        this.description = description;
        this.academyId = academyId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.maxCapacity = maxCapacity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.batchType = batchType;
        this.timing = timing;
        this.durationMonths = durationMonths;
        this.fees = fees;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBatchCode() { return batchCode; }
    public void setBatchCode(String batchCode) { this.batchCode = batchCode; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Long getAcademyId() { return academyId; }
    public void setAcademyId(Long academyId) { this.academyId = academyId; }
    
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getBatchType() { return batchType; }
    public void setBatchType(String batchType) { this.batchType = batchType; }
    
    public String getTiming() { return timing; }
    public void setTiming(String timing) { this.timing = timing; }
    
    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) { this.durationMonths = durationMonths; }
    
    public Double getFees() { return fees; }
    public void setFees(Double fees) { this.fees = fees; }
}
