package com.springbootlibrary.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name="messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;


    @Column(name="user_email")
    private String userEmail;

    @NonNull
    @Column(name="title")
    private String title;

    @NonNull
    @Column(name="question")
    private String question;


    @Column(name="admin_email")
    private String adminEmail;


    @Column(name="response")
    private String response;


    @Column(name="closed")
    private boolean closed;
}
