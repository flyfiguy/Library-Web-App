package com.springbootlibrary.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "checkout")
@Data
public class Checkout {
/*
    public checkout() {} //Default constructor
    public Checkout(String userEmail, String checkoutDate, String returnDate, Long bookId) {
        this.userEmail = userEmail;
        this.checkoutDate = checkoutDate;
        this.returnDate = returnDate;
        this.bookId = bookId;
    }
*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "user_email")
    private String userEmail;

    @NonNull
    @Column(name="checkout_date")
    private String checkoutDate;

    @NonNull
    @Column(name = "return_date")
    private String returnDate;

    @NonNull
    @Column(name = "book_id")
    private Long bookId;
}
