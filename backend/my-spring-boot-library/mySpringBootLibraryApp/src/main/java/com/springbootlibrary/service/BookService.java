package com.springbootlibrary.service;

import com.springbootlibrary.dao.BookRepository;
import com.springbootlibrary.dao.CheckoutRepository;
import com.springbootlibrary.dao.HistoryRepository;
import com.springbootlibrary.entity.Book;
import com.springbootlibrary.entity.Checkout;
import com.springbootlibrary.entity.History;
import com.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import org.hibernate.annotations.Check;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

//Need custom service that spring data rest does not include...
@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;
    private CheckoutRepository checkoutRepository;
    private HistoryRepository historyRepository;


    //"Constructor dependency injection" to set up the repositories throughout this file
    //Set up our book repository and checkout repository throughout this file
    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository, HistoryRepository historyRepository) {
        //Set it up, so we can use both repositories when we use this service
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.historyRepository = historyRepository;
    }

    public int deleteBookByTitle(String title) {
        return bookRepository.deleteByTitle(title);
    }

    public Book checkoutBook(String userEmail, Long bookId) throws Exception {

        //Get Book from database based on bookId
        //When you call a database, it returns an Optional of the entity type (Optional <Book>).
        Optional<Book> book = bookRepository.findById(bookId);

        // We do not want a user to checkout a book more than once.
        // Will return null if user does not have the book checked out. We will check to see if not null.
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

    //Test to see if a particular book is currently checked out by a particular user already.
    public Boolean checkoutBookByUser(String userEmail, Long bookId) {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if(validateCheckout != null) {
            return true;
        } else {
            return false;
        }
    }

    //Return the number of books a particular user has checked out.
    //The .size() returns the size of the list returned by the method.
    public int currentLoansCount(String userEmail) {
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

        //Get list of all books id's that the user has checked out
        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);

        List<Long> bookIdList = new ArrayList<>();

        //Put all the id's in a list
        for(Checkout i: checkoutList) {
            bookIdList.add(i.getBookId());
        }

        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);

        //Determine when book needs to be returned or how late it is.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for(Book book: books) {

            //Advanced matching using Optional, stream and Lambda for performance.
            //Optional: Wrap what is returned in Optional since .findFirst() returns Optional and a match may or may not be found.
            //stream: A sequential stream over the elements in the checkoutList collection
            //filter: Returns a stream consisting of the elements of this stream that match the given predicate (book id)
            //Lambda - x: each item in the checkoutList. Match it to the book from the books list using book id.
            //findFirst(): end search with the first found match.
            //With match, we have the book (from books list) and checkout (from checkoutList) objects with matching id.
            Optional<Checkout> checkout = checkoutList.stream().filter(x -> x.getBookId() == book.getId()).findFirst();

            //Using Optional to see if the checkout object is present (meaning a match was found for current book item in books list)
            if(checkout.isPresent()) {
                Date d1 = sdf.parse(checkout.get().getReturnDate());
                Date d2 = sdf.parse(LocalDate.now().toString());

                //Get difference between dates based on days.
                TimeUnit time = TimeUnit.DAYS;

                long difference_In_Time = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

                shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book, (int) difference_In_Time));
            }
        }
        return shelfCurrentLoansResponses;
    }

    //Return a book to the library
    public void returnBook (String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        //Verify there is a book with this email and book id
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(!book.isPresent() || validateCheckout == null) {
            throw new Exception("Book does not exist or not checked out by user");
        }

        //Update copies available for a book that is being checked in
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        bookRepository.save(book.get());

        //Remove the checkout record from the database.
        checkoutRepository.deleteById(validateCheckout.getId());

        //Save history object
        History history = new History(userEmail, validateCheckout.getCheckoutDate(), LocalDate.now().toString(),
                book.get().getTitle(), book.get().getAuthor(), book.get().getDescription(), book.get().getImg());

        //Save to history repo
        historyRepository.save(history);

    }

    public void renewLoan(String userEmail, Long bookId) throws Exception {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(validateCheckout == null) {
            throw new Exception("Book does not exist or it is not checked out by user");
        }

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = sdFormat.parse(validateCheckout.getReturnDate());
        Date d2 = sdFormat.parse(LocalDate.now().toString());

        //If today is return date or return date is not yet here...
        if(d1.compareTo(d2) > 0 || d1.compareTo(d2) == 0){
            //Add seven days to checkout
            validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
            //Save to db
            checkoutRepository.save(validateCheckout);
        }
    }
}
