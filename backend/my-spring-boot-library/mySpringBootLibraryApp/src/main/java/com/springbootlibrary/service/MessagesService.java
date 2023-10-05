package com.springbootlibrary.service;


import com.springbootlibrary.dao.MessageRepository;
import com.springbootlibrary.entity.Message;
import com.springbootlibrary.requestModels.AdminQuestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MessagesService {

    private MessageRepository messageRepository;

    @Autowired
    public MessagesService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void postMessage(Message messageRequest, String userEmail) {
        Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }

    /**
     * Search Database with message with ID passed in param
    * */
    public void putMessage(AdminQuestionRequest adminQuestionRequest, String userEmail) throws Exception {
        Optional<Message> message = messageRepository.findById(adminQuestionRequest.getId());
        if(!message.isPresent()) {
            throw new Exception("Message not found");
        }
        //We have the message. Get it. Set AdminEmail to this users email
        message.get().setAdminEmail(userEmail);
        //Set the response from admin
        message.get().setResponse(adminQuestionRequest.getResponse());
        //Set closed to true meaning the question is now closed
        message.get().setClosed(true);
        //Save message with new values to the database
        messageRepository.save(message.get());
    }

}
