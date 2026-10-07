package com.kashish.rental.dto;

public class PropertyResponse {

    private Long id;
    private String name;
    private String location;
    private String propertyType;
    private int bedrooms;
    private int bathrooms;
    private double area;
    private double rent;
    private String status;
    private String description;

    private UserResponse owner;

    public PropertyResponse() {
    }

    public PropertyResponse(
            Long id,
            String name,
            String location,
            String propertyType,
            int bedrooms,
            int bathrooms,
            double area,
            double rent,
            String status,
            String description,
            UserResponse owner) {

        this.id = id;
        this.name = name;
        this.location = location;
        this.propertyType = propertyType;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.area = area;
        this.rent = rent;
        this.status = status;
        this.description = description;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public double getArea() {
        return area;
    }

    public double getRent() {
        return rent;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public UserResponse getOwner() {
        return owner;
    }
}