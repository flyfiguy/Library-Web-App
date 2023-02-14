package com.springbootlibrary.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name="review")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Primary key
    @Column(name="id")
    private Long id;

    @NonNull
    @Column(name="user_email")
    private String userEmail;

    @NonNull
    @Column(name="date")
    @CreationTimestamp
    private Date date;

    @NonNull
    @Column(name="rating")
    private double rating;

    @NonNull
    @Column(name="book_id")
    private Long bookId;

    @NonNull
    @Column(name="review_description")
    private String reviewDescription;

}
