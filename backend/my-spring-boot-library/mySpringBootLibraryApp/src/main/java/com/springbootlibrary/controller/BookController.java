package com.springbootlibrary.controller;

import com.springbootlibrary.entity.Book;
import com.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import com.springbootlibrary.service.BookService;
import com.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//Add CrossOrigin URL of the calling application so react front end will
// be able to call this without getting a CORS error
@CrossOrigin("https://localhost:3000")
@RestController //Controller for rest API
@RequestMapping("/api/books") //To match the other data rest API's
public class BookController {

    //Instance of the custom BookService
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return bookService.currentLoans(userEmail);
    }


    //Exposed endpoint to return the number of books a particular user has checked out.
    //Expecting something in header with key="Authorization" for the jwt token value. Get the token and assign to the token var.
    //The token gets validated with Okta automatically.
    @GetMapping("/secure/currentloans/count")
    public int currentLoanCount(@RequestHeader(value="Authorization") String token){
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        System.out.println("Email : " + userEmail);
        return bookService.currentLoansCount(userEmail);
    }

    //Exposed endpoint for the service to see if a particular user currently has a particular book checked out.
    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean checkoutBookByUser(@RequestHeader(value="Authorization") String token,
                                      @RequestParam Long bookId) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        System.out.println("Email : " + userEmail);
        return bookService.checkoutBookByUser(userEmail, bookId);
    }

    //Exposed endpoint for the service to check out a book.
    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestHeader(value="Authorization") String token,
                             @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        System.out.println("Email : " + userEmail);
        return bookService.checkoutBook(userEmail, bookId);
    }

    @DeleteMapping("/secure/delete/book")
    public int deleteBookByTitle(@RequestHeader(value="Authorization") String token,
                                 @RequestParam String title) {
        return bookService.deleteBookByTitle(title);
    }

    @PutMapping("/secure/return")
    public void returnBook(@RequestHeader(value="Authorization") String token, @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        bookService.returnBook(userEmail, bookId);
    }

    @PutMapping("/secure/renew/loan")
    public void renewLoan(@RequestHeader(value="Authorization") String token, @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        bookService.renewLoan(userEmail, bookId);
    }
/*
    @PostMapping("/secure/checkout")
    public Book deleteBookByTitle(@RequestParam String title) throws Exception {
        return bookService.
    }
 */
}
