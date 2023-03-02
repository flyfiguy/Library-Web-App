package com.springbootlibrary.responsemodels;

import com.springbootlibrary.entity.Book;
import lombok.Data;

//This class will be used with a service that returns days left and book to the frontend shelf component.
@Data
public class ShelfCurrentLoansResponse {

    private Book book;

    private int daysLeft;

    public ShelfCurrentLoansResponse(Book book, int daysLeft) {
        this.book = book;
        this.daysLeft = daysLeft;
    }
}
