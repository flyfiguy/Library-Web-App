package com.springbootlibrary.controller;

import com.springbootlibrary.entity.Review;
import com.springbootlibrary.requestModels.ReviewRequest;
import com.springbootlibrary.service.ReviewService;
import com.springbootlibrary.utils.ExtractJWT;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private ReviewService reviewService;

    //Constructor to initialize the ReviewService
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value="Authorization") String token,
                           @RequestBody ReviewRequest reviewRequest) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        if(userEmail==null) {
            throw new Exception("User email is missing from jwt token");
        }
        reviewService.postReview(userEmail, reviewRequest);
    }

    @GetMapping("/secure/user/book")
    public Boolean reviewBookByUser(@RequestHeader(value="Authorization") String token, @RequestParam Long bookId) throws Exception {
       String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
       if(userEmail == null) {
           throw new Exception("User email is missing from jwt token");
       }
       return reviewService.userReviewListed(userEmail, bookId);
    }

}
