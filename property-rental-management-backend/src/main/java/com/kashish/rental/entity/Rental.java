package com.kashish.rental.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Entity
@Table(name = "RENTALS")
public class Rental {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "rental_seq"
    )
    @SequenceGenerator(
            name = "rental_seq",
            sequenceName = "RENTALS_SEQ",
            allocationSize = 1
    )
    private Long id;


    @ManyToOne
    @JoinColumn(name = "TENANT_ID")
    private User tenant;


    @NotNull(message = "Property is required")
    @ManyToOne
    @JoinColumn(name = "PROPERTY_ID")
    private Property property;


    @NotNull(message = "Start date is required")
    private LocalDate startDate;


    @NotNull(message = "End date is required")
    private LocalDate endDate;


    @Positive(message = "Rent amount must be greater than 0")
    private double rentAmount;


    @NotBlank(message = "Status is required")
    private String status;


    // Default constructor
    public Rental() {
    }


    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // Getter and Setter for tenant
    public User getTenant() {
        return tenant;
    }

    public void setTenant(User tenant) {
        this.tenant = tenant;
    }


    // Getter and Setter for property
    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }


    // Getter and Setter for startDate
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    // Getter and Setter for endDate
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    // Getter and Setter for rentAmount
    public double getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(double rentAmount) {
        this.rentAmount = rentAmount;
    }


    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}