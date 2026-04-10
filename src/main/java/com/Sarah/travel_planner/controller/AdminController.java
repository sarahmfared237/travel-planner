package com.Sarah.travel_planner.controller;

import com.Sarah.travel_planner.model.Destination;
import com.Sarah.travel_planner.service.DestinationService;
import com.Sarah.travel_planner.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final DestinationService destinationService;
    private final ExternalApiService externalApiService;

    @GetMapping("/destinations")
    public ResponseEntity<List<Destination>> getAllDestinations() {
        return ResponseEntity.ok(destinationService.getAllDestinations());
    }

    @GetMapping("/fetch/{region}")
    public ResponseEntity<List<Destination>> fetchFromApi(@PathVariable String region) {
        return ResponseEntity.ok(externalApiService.fetchDestinationsByRegion(region));
    }

    @PostMapping("/destinations")
    public ResponseEntity<Destination> addDestination(@RequestBody Destination destination) {
        return ResponseEntity.ok(destinationService.addDestination(destination));
    }

    @PostMapping("/destinations/bulk")
    public ResponseEntity<List<Destination>> bulkAdd(@RequestBody List<Destination> destinations) {
        return ResponseEntity.ok(destinationService.bulkAddDestinations(destinations));
    }

    @PutMapping("/destinations/{id}/approve")
    public ResponseEntity<Destination> approve(@PathVariable String id) {
        return ResponseEntity.ok(destinationService.approveDestination(id));
    }

    @DeleteMapping("/destinations/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        destinationService.deleteDestination(id);
        return ResponseEntity.noContent().build();
    }
}