package com.Sarah.travel_planner.repository;

import com.Sarah.travel_planner.model.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DestinationRepository extends MongoRepository<Destination, String> {
    Page<Destination> findByApprovedTrue(Pageable pageable);
    List<Destination> findByApprovedTrue();
    Page<Destination> findByApprovedTrueAndNameContainingIgnoreCase(String name, Pageable pageable);
}