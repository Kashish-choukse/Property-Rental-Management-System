package com.kashish.rental.service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kashish.rental.entity.Rental;
import com.kashish.rental.entity.User;
import com.kashish.rental.entity.Property;
import com.kashish.rental.repository.RentalRepository;
import com.kashish.rental.repository.UserRepository;
import com.kashish.rental.repository.PropertyRepository;

@Service
public class RentalService {

    private final RentalRepository repository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    public RentalService(
            RentalRepository repository,
            UserRepository userRepository,
            PropertyRepository propertyRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    // Add rental
    @Transactional
    public Rental addRental(
            Rental rental,
            String email) {

    	User tenant = userRepository.findByEmail(email)
    	        .orElseThrow(() ->
    	                new RuntimeException("Tenant not found"));
        
        if (!"TENANT".equalsIgnoreCase(tenant.getRole())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selected user is not a tenant"
            );
        }

        Property property = propertyRepository.findById(rental.getProperty().getId())
                .orElseThrow(() ->
                        new RuntimeException("Property not found"));

        // Check whether property is already rented
        if ("RENTED".equalsIgnoreCase(property.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Property is already rented"
            );
        }
        
        if (!rental.getEndDate().isAfter(rental.getStartDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date must be after start date"
            );
        }
        
     // Check rental status
        if (!"ACTIVE".equalsIgnoreCase(rental.getStatus())
                && !"COMPLETED".equalsIgnoreCase(rental.getStatus())
                && !"CANCELLED".equalsIgnoreCase(rental.getStatus())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid rental status"
            );
        }
        
     // Check rental amount
        if (rental.getRentAmount() != property.getRent()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rental amount must match property rent"
            );
        }

        rental.setTenant(tenant);
        rental.setProperty(property);

        // Change property status to RENTED
        property.setStatus("RENTED");
        propertyRepository.save(property);

        return repository.save(rental);
    }

    // Get all rentals
    public List<Rental> getAllRentals() {
        return repository.findAll();
    }

    // Get rental by ID
    public Rental getRentalById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Rental not found with id: " + id));
    }

    // Update rental
    
    	
    @Transactional
    public Rental updateRental(
    	        Long id,
    	        Rental updatedRental,
    	        String email) {
        Rental existingRental = getRentalById(id);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if ("TENANT".equalsIgnoreCase(user.getRole())) {

            if (!existingRental.getTenant().getId()
                    .equals(user.getId())) {

                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You can update only your own rental"
                );
            }

        } else if ("OWNER".equalsIgnoreCase(user.getRole())) {

            if (existingRental.getProperty().getOwner() == null
                    || !existingRental.getProperty()
                            .getOwner()
                            .getId()
                            .equals(user.getId())) {

                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You can update rentals only for your own properties"
                );
            }

        } else {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to update rentals"
            );
        }
        
     // Validate rental dates
        if (!updatedRental.getEndDate()
                .isAfter(updatedRental.getStartDate())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date must be after start date"
            );
        }

        // Validate rental amount
        if (updatedRental.getRentAmount()
                != existingRental.getProperty().getRent()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rental amount must match property rent"
            );
        }

        // Validate rental status
        if (!"ACTIVE".equalsIgnoreCase(updatedRental.getStatus())
                && !"COMPLETED".equalsIgnoreCase(updatedRental.getStatus())
                && !"CANCELLED".equalsIgnoreCase(updatedRental.getStatus())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid rental status"
            );
        }

        existingRental.setStartDate(updatedRental.getStartDate());
        existingRental.setEndDate(updatedRental.getEndDate());
        existingRental.setRentAmount(updatedRental.getRentAmount());
        existingRental.setStatus(updatedRental.getStatus());

     // If rental is completed or cancelled,
     // make the property available again
     if ("COMPLETED".equalsIgnoreCase(updatedRental.getStatus())
             || "CANCELLED".equalsIgnoreCase(updatedRental.getStatus())) {

         Property property = existingRental.getProperty();

         property.setStatus("AVAILABLE");
         propertyRepository.save(property);
     }

        return repository.save(existingRental);
    }

    // Delete rental
    
    	@Transactional
    	public void deleteRental(
    	        Long id,
    	        String email) {

    	    Rental rental = getRentalById(id);

    	    User user = userRepository.findByEmail(email)
    	            .orElseThrow(() ->
    	                    new RuntimeException("User not found"));

    	    // TENANT can delete only their own rental
    	    if ("TENANT".equalsIgnoreCase(user.getRole())) {

    	        if (!rental.getTenant().getId()
    	                .equals(user.getId())) {

    	            throw new ResponseStatusException(
    	                    HttpStatus.FORBIDDEN,
    	                    "You can delete only your own rental"
    	            );
    	        }

    	    // OWNER can delete only rentals of their own properties
    	    } else if ("OWNER".equalsIgnoreCase(user.getRole())) {

    	        if (rental.getProperty().getOwner() == null
    	                || !rental.getProperty()
    	                        .getOwner()
    	                        .getId()
    	                        .equals(user.getId())) {

    	            throw new ResponseStatusException(
    	                    HttpStatus.FORBIDDEN,
    	                    "You can delete rentals only for your own properties"
    	            );
    	        }

    	    } else {

    	        throw new ResponseStatusException(
    	                HttpStatus.FORBIDDEN,
    	                "You are not allowed to delete rentals"
    	        );
    	    }

    	    // Get the property associated with this rental
    	    Property property = rental.getProperty();

    	    // Make the property available again
    	    property.setStatus("AVAILABLE");
    	    propertyRepository.save(property);

    	    // Delete the rental
    	    repository.delete(rental);
    	}

    // Get rentals by tenant
    public List<Rental> getRentalsByTenant(Long tenantId) {
        return repository.findByTenantId(tenantId);
    }
    
 // Get rentals by tenant email
    public List<Rental> getRentalsByTenantEmail(String email) {
        return repository.findByTenantEmail(email);
    }

    // Get rentals by property
    public List<Rental> getRentalsByProperty(Long propertyId) {
        return repository.findByPropertyId(propertyId);
    }
}