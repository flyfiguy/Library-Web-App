package com.springbootlibrary.requestModels;

import lombok.Data;

//Used for object sent from React application to backend to update current message with response from admin
@Data
public class AdminQuestionRequest {
    private Long id;
    private String response;
}
