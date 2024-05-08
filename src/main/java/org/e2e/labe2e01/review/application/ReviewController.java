package org.e2e.labe2e01.review.application;

import org.e2e.labe2e01.review.domain.Review;
import org.e2e.labe2e01.review.domain.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping()
    public ResponseEntity<String> createNewReview (@RequestBody Review newReview, @RequestParam Long rideId){
        reviewService.createNewReview(newReview, rideId);
        return ResponseEntity.ok("Review created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview (@PathVariable Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review deleted");
    }
}
