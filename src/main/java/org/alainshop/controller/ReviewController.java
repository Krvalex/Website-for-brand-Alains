package org.alainshop.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.model.Review;
import org.alainshop.model.User;
import org.alainshop.service.ProductService;
import org.alainshop.service.ReviewService;
import org.alainshop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    ReviewService reviewService;
    UserService userService;
    ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Review> addReview(
            Principal principal,
            @RequestParam Long productId,
            @RequestParam String text,
            @RequestParam int rating) {
        User user = userService.getByPrincipal(principal);
        Review review = reviewService.addReview(productId, user.getId(), text, rating);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProduct(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsByProduct(productId);
        return ResponseEntity.ok(reviews);
    }
}
