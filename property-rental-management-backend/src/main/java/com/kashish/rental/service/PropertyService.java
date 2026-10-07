
package com.kashish.rental.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kashish.rental.entity.Property;
import com.kashish.rental.entity.User;
import com.kashish.rental.repository.PropertyRepository;
import com.kashish.rental.repository.UserRepository;

@Service
public class PropertyService {

    private final PropertyRepository repository;
    private final UserRepository userRepository;

    public PropertyService(
            PropertyRepository repository,
            UserRepository userRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
    }

    // Add Property
    public Property addProperty(Property property, String email) {

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Owner not found"));

        property.setOwner(owner);

        validateStatus(property.getStatus());

        return repository.save(property);
    }

    // Get All Properties
    public List<Property> getAllProperties() {
        return repository.findAll();
    }

    // Get Property By ID
    public Property getPropertyById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Property not found with id: " + id));
    }

    // Update Property
    public Property updateProperty(
            Long id,
            Property updated,
            String email) {

        Property existing =
                getPropertyOwnedByUser(id, email);

        validateStatus(updated.getStatus());

        existing.setName(updated.getName());
        existing.setLocation(updated.getLocation());
        existing.setPropertyType(updated.getPropertyType());
        existing.setBedrooms(updated.getBedrooms());
        existing.setBathrooms(updated.getBathrooms());
        existing.setArea(updated.getArea());
        existing.setRent(updated.getRent());
        existing.setStatus(updated.getStatus());
        existing.setDescription(updated.getDescription());

        return repository.save(existing);
    }

    // Delete Property
    public void deleteProperty(
            Long id,
            String email) {

        Property existing =
                getPropertyOwnedByUser(id, email);

        repository.delete(existing);
    }

    // Search By Location
    public List<Property> searchByLocation(String location) {

        return repository.findByLocationContainingIgnoreCase(location);
    }

    // Filter By Status
    public List<Property> filterByStatus(String status) {

        return repository.findByStatusIgnoreCase(status);
    }

    // Filter By Property Type
    public List<Property> filterByType(String type) {

        return repository.findByPropertyTypeIgnoreCase(type);
    }

    // Filter By Maximum Rent
    public List<Property> filterByMaximumRent(double rent) {

        return repository.findByRentLessThanEqual(rent);
    }

    // Get Properties Of An Owner
    public List<Property> getPropertiesByOwner(Long ownerId) {

        return repository.findPropertiesByOwnerId(ownerId);
    }

    // Check Property Ownership
    private Property getPropertyOwnedByUser(
            Long propertyId,
            String email) {

        Property property =
                getPropertyById(propertyId);

        if (property.getOwner() == null) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Property has no owner");
        }

        if (!property.getOwner()
                .getEmail()
                .equalsIgnoreCase(email)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can modify only your own properties");
        }

        return property;
    }

    // Validate Property Status
    private void validateStatus(String status) {

        if (status == null ||
                (!status.equalsIgnoreCase("AVAILABLE")
                && !status.equalsIgnoreCase("RENTED"))) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Property status must be AVAILABLE or RENTED");
        }
    }
}