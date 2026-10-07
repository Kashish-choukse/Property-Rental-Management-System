package com.kashish.rental.controller;


import org.springframework.security.core.Authentication;
import com.kashish.rental.dto.PropertyResponse;
import com.kashish.rental.dto.UserResponse;
import com.kashish.rental.entity.Property;
import com.kashish.rental.entity.User;
import com.kashish.rental.service.PropertyService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    private final PropertyService service;

    public PropertyController(PropertyService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyResponse addProperty(
            @Valid @RequestBody Property property,
            Authentication authentication) {

        Property savedProperty =
                service.addProperty(property, authentication.getName());

        return convertToResponse(savedProperty);
    }

    @GetMapping
    public List<PropertyResponse> getAllProperties() {

        return service.getAllProperties()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PropertyResponse getProperty(@PathVariable Long id) {

        Property property = service.getPropertyById(id);

        return convertToResponse(property);
    }

    @PutMapping("/{id}")
    public PropertyResponse updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody Property property,
            Authentication authentication) {

        Property updatedProperty =
                service.updateProperty(
                        id,
                        property,
                        authentication.getName()
                );

        return convertToResponse(updatedProperty);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProperty(
            @PathVariable Long id,
            Authentication authentication) {

        service.deleteProperty(
                id,
                authentication.getName()
        );
    }

    @GetMapping("/search")
    public List<PropertyResponse> search(@RequestParam String location) {

        return service.searchByLocation(location)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/filter/status")
    public List<PropertyResponse> filterStatus(@RequestParam String status) {

        return service.filterByStatus(status)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/filter/type")
    public List<PropertyResponse> filterType(@RequestParam String type) {

        return service.filterByType(type)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/filter/rent")
    public List<PropertyResponse> filterRent(@RequestParam double maxRent) {

        return service.filterByMaximumRent(maxRent)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PropertyResponse> getPropertiesByOwner(
            @PathVariable Long ownerId) {

        return service.getPropertiesByOwner(ownerId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Convert Property entity → PropertyResponse
    private PropertyResponse convertToResponse(Property property) {

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

        return new PropertyResponse(
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
}