package com.springbootlibrary.service;

import com.springbootlibrary.dao.BookRepository;
import com.springbootlibrary.dao.CheckoutRepository;
import com.springbootlibrary.entity.Book;
import com.springbootlibrary.entity.Checkout;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

//Need custom service that spring data rest does not include...
@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;
    private CheckoutRepository checkoutRepository;


    //Constructor dependency injection to set up the repositories
    //Set up our book repository and checkout repository throughout this file
    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository, CheckoutRepository checkoutRepository1) {
        //Set it up, so we can use both repositories when we use this service
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public int deleteBookByTitle(String title) {
        return bookRepository.deleteByTitle(title);
    }

    public Book checkoutBook(String userEmail, Long bookId) throws Exception {

        //Get Book from database based on bookId
        Optional<Book> book = bookRepository.findById(bookId);

        //Will return null if user does not have the book checked out.
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        //Make sure book has copies available
        //Make sure user does not have book checked out
        //Make sure the book actually exists
        if(!book.isPresent() || validateCheckout != null || book.get().getCopiesAvailable() <= 0) {
            throw new Exception("Book doesn't exist or already checked out by user.");
        }

        //Decrement the book available counter
        book.get().setCopiesAvailable(book.get().getCopiesAvailable()-1);

        //Save updated record to db
        bookRepository.save(book.get());

        //Create new Checkout object that we can save to db as new record
        Checkout checkout = new Checkout(
                userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(),
                book.get().getId()
        );

        //Save checkout to database
        checkoutRepository.save(checkout);

        //Return book to caller
        return book.get();
    }

    public Boolean checkoutBookByUser(String userEmail, Long bookId) {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if(validateCheckout != null) {
            return true;
        } else {
            return false;
        }
    }

    public int currentLoansCount(String userEmail) {
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }
}
