package com.springbootlibrary.dao;

import com.springbootlibrary.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
    Checkout findByUserEmailAndBookId(String userEmail, Long bookId);

    //Return a list of books a particular user has checked out.
    List<Checkout> findBooksByUserEmail(String userEmail);
}
