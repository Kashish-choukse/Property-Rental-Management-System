package com.kashish.rental.controller;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.kashish.rental.dto.PropertyResponse;
import com.kashish.rental.dto.RentalResponse;
import com.kashish.rental.dto.UserResponse;
import com.kashish.rental.entity.Property;
import com.kashish.rental.entity.Rental;
import com.kashish.rental.entity.User;
import com.kashish.rental.service.RentalService;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService service;

    public RentalController(RentalService service) {
        this.service = service;
    }

    // Add rental
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalResponse addRental(
            @Valid @RequestBody Rental rental,
            Authentication authentication) {

        Rental savedRental =
                service.addRental(
                        rental,
                        authentication.getName()
                );

        return convertToResponse(savedRental);
    }

    // Get all rentals
    @GetMapping
    public List<RentalResponse> getAllRentals(
            Authentication authentication) {

        String email = authentication.getName();

        return service.getRentalsByTenantEmail(email)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get rental by ID
    @GetMapping("/{id}")
    public RentalResponse getRentalById(@PathVariable Long id) {

        Rental rental = service.getRentalById(id);

        return convertToResponse(rental);
    }

    // Get rentals by tenant
    @GetMapping("/tenant/{tenantId}")
    public List<RentalResponse> getRentalsByTenant(
            @PathVariable Long tenantId) {

        return service.getRentalsByTenant(tenantId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get rentals by property
    @GetMapping("/property/{propertyId}")
    public List<RentalResponse> getRentalsByProperty(
            @PathVariable Long propertyId) {

        return service.getRentalsByProperty(propertyId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Update rental
    @PutMapping("/{id}")
    public RentalResponse updateRental(
            @PathVariable Long id,
            @Valid @RequestBody Rental rental,
            Authentication authentication) {

        Rental updatedRental =
                service.updateRental(
                        id,
                        rental,
                        authentication.getName()
                );

        return convertToResponse(updatedRental);
    }

    // Delete rental
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRental(
            @PathVariable Long id,
            Authentication authentication) {

        service.deleteRental(
                id,
                authentication.getName()
        );
    }

    // Convert Rental entity → RentalResponse
    private RentalResponse convertToResponse(Rental rental) {

        User tenant = rental.getTenant();

        UserResponse tenantResponse = null;

        if (tenant != null) {
            tenantResponse = new UserResponse(
                    tenant.getId(),
                    tenant.getName(),
                    tenant.getEmail(),
                    tenant.getRole(),
                    tenant.getPhone()
            );
        }

        Property property = rental.getProperty();

        PropertyResponse propertyResponse = null;

        if (property != null) {

            User owner = property.getOwner();

            UserResponse ownerResponse = null;

            if (owner != null) {
                ownerResponse = new UserResponse(
                        owner.getId(),
                        owner.getName(),
                        owner.getEmail(),
                        owner.getRole(),
                        owner.getPhone()
                );
            }

            propertyResponse = new PropertyResponse(
                    property.getId(),
                    property.getName(),
                    property.getLocation(),
                    property.getPropertyType(),
                    property.getBedrooms(),
                    property.getBathrooms(),
                    property.getArea(),
                    property.getRent(),
                    property.getStatus(),
                    property.getDescription(),
                    ownerResponse
            );
        }

        return new RentalResponse(
                rental.getId(),
                tenantResponse,
                propertyResponse,
                rental.getStartDate(),
                rental.getEndDate(),
                rental.getRentAmount(),
                rental.getStatus()
        );
    }
}