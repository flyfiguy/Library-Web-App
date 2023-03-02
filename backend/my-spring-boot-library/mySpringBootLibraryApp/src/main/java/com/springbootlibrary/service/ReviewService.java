package com.springbootlibrary.service;

import com.springbootlibrary.dao.BookRepository;
import com.springbootlibrary.dao.ReviewRepository;
import com.springbootlibrary.entity.Review;
import com.springbootlibrary.requestModels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
public class ReviewService {

    //private BookRepository bookRepository;
    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(/*BookRepository bookRepository,*/ ReviewRepository reviewRepository) {
        //this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
    }

    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception {
        //See if user already left a review for this book
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, reviewRequest.getBookId());

        //If user already left a review, throw exception indicating one was already created...
        if(validateReview != null) {
            throw  new Exception("Review already created");
        }

        Review review = new Review();
        review.setBookId(reviewRequest.getBookId());
        review.setRating(reviewRequest.getRating());
        review.setUserEmail(userEmail);
        if(reviewRequest.getReviewDescription().isPresent()) {
            //Since review description is an Optional in the ReviewRequest object, you have to map the value this way...
            review.setReviewDescription(reviewRequest.getReviewDescription().map(Object::toString).orElse(null));
        }
        review.setDate(Date.valueOf(LocalDate.now()));
        reviewRepository.save(review);
    }

    public boolean userReviewListed(String userEmail, Long bookId) {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, bookId);
        if(validateReview != null)
            return true;
        else
            return false;
    }

}
