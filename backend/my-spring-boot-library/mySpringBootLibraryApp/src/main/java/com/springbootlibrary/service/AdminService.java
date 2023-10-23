package com.springbootlibrary.service;

import com.springbootlibrary.dao.BookRepository;
import com.springbootlibrary.dao.CheckoutRepository;
import com.springbootlibrary.dao.ReviewRepository;
import com.springbootlibrary.entity.Book;
import com.springbootlibrary.requestModels.AddBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@Transactional
public class AdminService {
    private BookRepository bookRepository;
    private ReviewRepository reviewRepository;
    private CheckoutRepository checkoutRepository;

    @Autowired
    public AdminService (BookRepository bookRepository, ReviewRepository reviewRepository, CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public void increaseBookQuantity(Long bookId) throws Exception {

        //Get book from db based on bookId (unique key)
        Optional<Book> book = bookRepository.findById(bookId);

        //If no book found, throw an exception
        if(!book.isPresent()) {
            throw new Exception("increaseBookQuantity: Book not found");
        }

        //Increase count of books available
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        //Increase overall book count for the book
        book.get().setCopies(book.get().getCopies()+1);

        //Save book update to repo/database
        bookRepository.save(book.get());

    }

    public void decreaseBookQuantity(Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        if(!book.isPresent() || book.get().getCopiesAvailable() <=0 || book.get().getCopies() <= 0) {
            throw new Exception("decreaseBookQuantity: Either book not found or quantity locked due to either copies available or overall copies being <= 0");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable()-1);
        book.get().setCopies(book.get().getCopies()-1);

        bookRepository.save(book.get());
    }

    public void postBook(AddBookRequest addBookRequest){
        Book book = new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setDescription(addBookRequest.getDescription());
        book.setCopies(addBookRequest.getCopies());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setCategory(addBookRequest.getCategory());
        book.setImg(addBookRequest.getImg());
        bookRepository.save(book);
    }

    public void deleteBook(Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        if(!book.isPresent()) {
            throw new Exception("AdminService: Book not found");
        }

        //Delete book and all its checkouts and reviews
        bookRepository.delete(book.get());
        checkoutRepository.deleteAllByBookId(bookId);
        reviewRepository.deleteAllByBookId(bookId);
    }

}
