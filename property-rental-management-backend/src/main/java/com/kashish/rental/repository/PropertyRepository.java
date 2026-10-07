package com.kashish.rental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kashish.rental.entity.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {
	
	

    // Search properties by location
    List<Property> findByLocationContainingIgnoreCase(String location);

    // Filter properties by status
    List<Property> findByStatusIgnoreCase(String status);

    // Filter properties by property type
    List<Property> findByPropertyTypeIgnoreCase(String propertyType);

    // Filter properties by maximum rent
    List<Property> findByRentLessThanEqual(double rent);

    // Get properties belonging to a particular owner
    @Query(value = "SELECT * FROM PROPERTIES WHERE OWNER_ID = :ownerId",
           nativeQuery = true)
    List<Property> findPropertiesByOwnerId(@Param("ownerId") Long ownerId);
    
    @Query(
    	    value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
    	            "FROM PROPERTIES WHERE OWNER_ID = :ownerId",
    	    nativeQuery = true
    	)
    	int existsByOwnerId(@Param("ownerId") Long ownerId);
    
    
}