package com.springbootlibrary.controller;

import com.springbootlibrary.entity.Book;
import com.springbootlibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//Add CrossOrigin so react front end will be able to call this without getting a cors error
@CrossOrigin("http://localhost:3000")
@RestController //Controller for rest API
@RequestMapping("/api/books") //To match the other data rest API's
public class BookController {

    //Instance of the custom BookService
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoanCount(){
        String userEmail = "testuser@email.com";
        return bookService.currentLoansCount(userEmail);
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean checkoutBookByUser(@RequestParam Long bookId) {
        String userEmail = "testuser@email.com";
        return bookService.checkoutBookByUser(userEmail, bookId);
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestParam Long bookId) throws Exception {
        String userEmail = "testuser@email.com"; //Will extract this from payload that comes in later on...
        return bookService.checkoutBook(userEmail, bookId);
    }

    @DeleteMapping("/secure/delete/book")
    public int deleteBookByTitle(@RequestParam String title) {
        return bookService.deleteBookByTitle(title);
    }
/*
    @PostMapping("/secure/checkout")
    public Book deleteBookByTitle(@RequestParam String title) throws Exception {
        return bookService.
    }
 */
}
