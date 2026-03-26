package com.example.academy.academy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateAcademyRequest {
    
    @NotBlank(message = "Academy name is required")
    @Size(max = 100, message = "Academy name must not exceed 100 characters")
    private String name;
    
    @NotBlank(message = "Academy code is required")
    @Pattern(regexp = "^[A-Z0-9]{3,10}$", message = "Academy code must be 3-10 uppercase letters and numbers")
    private String code;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String address;
    
    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must not exceed 50 characters")
    private String city;
    
    @NotBlank(message = "State is required")
    @Size(max = 50, message = "State must not exceed 50 characters")
    private String state;
    
    @NotBlank(message = "Country is required")
    @Size(max = 50, message = "Country must not exceed 50 characters")
    private String country;
    
    @Pattern(regexp = "^[0-9]{6,10}$", message = "Postal code must be 6-10 digits")
    private String postalCode;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be valid")
    private String phoneNumber;
    
    @jakarta.validation.constraints.Email(message = "Email should be valid")
    private String email;
    
    @Size(max = 100, message = "Website must not exceed 100 characters")
    private String website;
    
    private String status = "ACTIVE";
    
    // Default constructor
    public CreateAcademyRequest() {}
    
    // All args constructor
    public CreateAcademyRequest(String name, String code, String description, String address,
                               String city, String state, String country, String postalCode,
                               String phoneNumber, String email, String website, String status) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.website = website;
        this.status = status;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
