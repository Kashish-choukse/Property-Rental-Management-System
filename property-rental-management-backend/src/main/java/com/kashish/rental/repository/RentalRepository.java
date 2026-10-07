package com.kashish.rental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kashish.rental.entity.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByTenantId(Long tenantId);
    
    List<Rental> findByTenantEmail(String email);

    List<Rental> findByPropertyId(Long propertyId);

    @Query(
        value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
                "FROM RENTALS WHERE TENANT_ID = :tenantId",
        nativeQuery = true
    )
    int existsByTenantId(@Param("tenantId") Long tenantId);
}