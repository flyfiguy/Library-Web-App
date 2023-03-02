package com.springbootlibrary.requestModels;

import lombok.Data;

import java.util.Optional;

@Data //Lombok - handles boiler-plate getters and setters
public class ReviewRequest {
    private double rating;

    private Long bookId;

    //Optional since description is not required
    private Optional<String> reviewDescription;
}
