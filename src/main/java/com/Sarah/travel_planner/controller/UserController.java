package com.Sarah.travel_planner.controller;

import com.Sarah.travel_planner.model.Destination;
import com.Sarah.travel_planner.service.DestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final DestinationService destinationService;

    @GetMapping("/destinations")
    public ResponseEntity<Page<Destination>> getDestinations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(destinationService.getApprovedDestinations(page, size));
    }

    @GetMapping("/destinations/search")
    public ResponseEntity<Page<Destination>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(destinationService.searchDestinations(keyword, page, size));
    }

    // Toggle a destination in/out of the user's wishlist
    @PostMapping("/destinations/{id}/want-to-visit")
    public ResponseEntity<List<String>> toggleWantToVisit(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                destinationService.toggleWantToVisit(id, userDetails.getUsername())
        );
    }

    // Get the user's full wishlist with destination details
    @GetMapping("/wishlist")
    public ResponseEntity<List<Destination>> getWishlist(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                destinationService.getWishlist(userDetails.getUsername())
        );
    }
}