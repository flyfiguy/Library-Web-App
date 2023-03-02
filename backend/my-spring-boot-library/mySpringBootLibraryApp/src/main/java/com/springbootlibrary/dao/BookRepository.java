package com.springbootlibrary.dao;

import com.springbootlibrary.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByTitleContaining(@RequestParam("title") String title, Pageable pageable);
    Page<Book> findByCategory(@RequestParam("category") String category, Pageable pageable);

    int deleteByTitle(@RequestParam("Title") String title);

    //This is not automatically set up for us. Need to set this up using SQL to Query database.
    //@Query: Override our findBooksByBookIds because Spring Boot will not understand what we need from this.
    //@Query: o is a row/book in the book table
    //@Query: Select each row in book table where id column is in book_ids list
    @Query("Select o from Book o where id in :book_ids")
    List<Book> findBooksByBookIds(@Param("book_ids") List<Long> bookId);
/*
    @Transactional
    @Modifying
    //@DeleteMapping("/secure/delete")
    @Query("delete from Book b where b.title=:title")
    int deleteByTitle(@Param("title") String title);



    @Transactional
    @Modifying
    @Query("insert into Book (title, author, description, copies, copies_available, category, img) values(:title, :author, :description, :copies, :copiesAvailable, :category, :img)")
    Book save(@RequestParam("title") String title,
                      @RequestParam("author") String author,
                      @RequestParam("description") String description,
                      @RequestParam("copies") int copies,
                      @RequestParam("copiesAvailable") int copiesAvailable,
                      @RequestParam("category") String category,
                      @RequestParam("img") byte[] img);
*/
}

