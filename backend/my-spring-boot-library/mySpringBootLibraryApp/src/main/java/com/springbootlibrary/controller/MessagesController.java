package com.springbootlibrary.controller;

import com.springbootlibrary.entity.Message;
import com.springbootlibrary.requestModels.AdminQuestionRequest;
import com.springbootlibrary.service.MessagesService;
import com.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/messages")
public class MessagesController {
    private MessagesService messagesService;

    @Autowired
    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @PostMapping("/secure/add/message")
    public void postMessage(@RequestHeader(value="Authorization") String token,
                            @RequestBody Message messageRequest) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        messagesService.postMessage(messageRequest, userEmail);
    }

    //API endpoint for "admin"s only based on the URI mapping
    @PutMapping("/secure/admin/message")
    public void putMessage(@RequestHeader(value="AUthorization") String token,
                           @RequestBody AdminQuestionRequest adminQuestionRequest) throws Exception {
        //Get the user email from the token
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        //Get the userType from the token
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");

        //If not an admin, throw an exception with message for front end.
        if(admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only.");
        }

        //They are an admin if they make it here. Call the message service to put the message
        messagesService.putMessage(adminQuestionRequest, userEmail);
    }

}
