package com.springbootlibrary.entity;

import lombok.*;
import jakarta.persistence.*; //Use to be javax.persistence.*


@Entity //Specifies this class is an entity for persistence
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name="book")//Name of table in database
@Data //Lombok - create all getters and setters for class
public class Book {

    //Values for/of book
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Primary key
    @Column(name="id") //Name of column in database table
    private Long id;

    @Column(name = "title") //Name in database table
    @NonNull
    private String title;

    @Column(name = "author") //Name in database table
    @NonNull
    private String author;

    @Column(name = "description") //Name in database table
    @NonNull
    private String description;

    @Column(name = "copies") //Name in database table
    @NonNull
    private int copies;

    @Column(name = "copies_available") //Name in database table
    @NonNull
    private int copiesAvailable;

    @Column(name = "category") //Name in database table
    @NonNull
    private String category;

    @Lob
    @Column(name = "img") //Name in database table
    private String img;
}