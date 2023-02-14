package com.springbootlibrary.controller;

import com.springbootlibrary.entity.Message;
import com.springbootlibrary.service.MessagesService;
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
    public void postMessage(@RequestBody Message messageRequest) {
        String userEmail = messageRequest.getUserEmail();
        messagesService.postMessage(messageRequest, userEmail);
    }

}
