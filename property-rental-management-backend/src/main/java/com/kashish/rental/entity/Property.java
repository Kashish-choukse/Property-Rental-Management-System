package com.kashish.rental.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "PROPERTIES")
public class Property {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "property_seq")
	@SequenceGenerator(
	        name = "property_seq",
	        sequenceName = "PROPERTIES_SEQ",
	        allocationSize = 1
	)
	private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    @NotBlank
    @Column(name = "PROPERTY_TYPE")
    private String propertyType;

    @Min(0)
    private int bedrooms;

    @Min(0)
    private int bathrooms;

    @Min(0)
    private double area;

    @Positive
    private double rent;

    @NotBlank
    private String status;

    @Column(length = 500)
    private String description;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OWNER_ID")
    private User owner;
    
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Property() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public int getBedrooms() { return bedrooms; }
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }

    public int getBathrooms() { return bathrooms; }
    public void setBathrooms(int bathrooms) { this.bathrooms = bathrooms; }

    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }

    public double getRent() { return rent; }
    public void setRent(double rent) { this.rent = rent; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    
}
