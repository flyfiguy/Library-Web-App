package com.springbootlibrary.dao;

import com.springbootlibrary.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
    Checkout findByUserEmailAndBookId(String userEmail, Long bookId);

    //Return a list of books a particular user has checked out.
    List<Checkout> findBooksByUserEmail(String userEmail);

    //@Modifying means this will modify the record - it will delete it
    //@Query: Note that "bookId" refers to the instance var name given for the db field book_id in the entity/table object "Checkout".
    @Modifying
    @Query("delete from Checkout where bookId in :book_id")
    void deleteAllByBookId(@Param("book_id") Long bookId);
}
