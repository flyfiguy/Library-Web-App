package com.springbootlibrary.dao;

import com.springbootlibrary.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

public interface MessageRepository extends JpaRepository<Message, Long> {
    //All open and closed (have responses) messages
    Page<Message> findByUserEmail(@RequestParam("user_email") String userEmail, Pageable pageable);

    //Admin only wants to see questions that have not been responded to...for all users/clients.
    Page<Message> findByClosed(@RequestParam("closed") boolean closed, Pageable pageable);

}
