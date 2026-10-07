package com.kashish.rental.dto;

import java.time.LocalDate;

public class RentalResponse {

    private Long id;

    private UserResponse tenant;

    private PropertyResponse property;

    private LocalDate startDate;

    private LocalDate endDate;

    private double rentAmount;

    private String status;

    public RentalResponse() {
    }

    public RentalResponse(
            Long id,
            UserResponse tenant,
            PropertyResponse property,
            LocalDate startDate,
            LocalDate endDate,
            double rentAmount,
            String status) {

        this.id = id;
        this.tenant = tenant;
        this.property = property;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rentAmount = rentAmount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public UserResponse getTenant() {
        return tenant;
    }

    public PropertyResponse getProperty() {
        return property;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getRentAmount() {
        return rentAmount;
    }

    public String getStatus() {
        return status;
    }
}