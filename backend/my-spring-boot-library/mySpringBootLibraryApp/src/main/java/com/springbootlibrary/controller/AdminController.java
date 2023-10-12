package com.springbootlibrary.controller;

import com.springbootlibrary.requestModels.AddBookRequest;
import com.springbootlibrary.service.AdminService;
import com.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/secure/add/book")
    public void postBook(@RequestHeader(value="Authorization")String token,
                         @RequestBody AddBookRequest addBookRequest) throws  Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if(admin == null || !admin.equals("admin")) {
            throw new Exception("AdminController: Administration page only");
        }
        adminService.postBook(addBookRequest);
    }

}
