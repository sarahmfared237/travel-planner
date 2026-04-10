package com.Sarah.travel_planner.service;

import com.Sarah.travel_planner.model.Destination;
import com.Sarah.travel_planner.model.User;
import com.Sarah.travel_planner.repository.DestinationRepository;
import com.Sarah.travel_planner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final UserRepository userRepository;

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Page<Destination> getApprovedDestinations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return destinationRepository.findByApprovedTrue(pageable);
    }

    public Page<Destination> searchDestinations(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return destinationRepository.findByApprovedTrueAndNameContainingIgnoreCase(keyword, pageable);
    }

    public Destination addDestination(Destination destination) {
        destination.setApproved(false);
        return destinationRepository.save(destination);
    }

    public List<Destination> bulkAddDestinations(List<Destination> destinations) {
        destinations.forEach(d -> d.setApproved(false));
        return destinationRepository.saveAll(destinations);
    }

    public Destination approveDestination(String id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found"));
        destination.setApproved(true);
        return destinationRepository.save(destination);
    }

    public void deleteDestination(String id) {
        destinationRepository.deleteById(id);
    }

    // Toggle: adds or removes destination from the user's wishlist
    public List<String> toggleWantToVisit(String destinationId, String username) {
        // Verify the destination actually exists
        destinationRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> wishlist = user.getWantToVisitDestinationIds();

        if (wishlist.contains(destinationId)) {
            wishlist.remove(destinationId);
        } else {
            wishlist.add(destinationId);
        }

        userRepository.save(user);
        return wishlist;
    }

    // Returns the full destination objects from the user's wishlist
    public List<Destination> getWishlist(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return destinationRepository.findAllById(user.getWantToVisitDestinationIds());
    }
}