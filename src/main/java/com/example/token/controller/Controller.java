package com.example.token.controller;

import com.example.token.service.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    @Autowired
    MyServiceImpl myService;

    @GetMapping("/get-token")
    public String getToken() {
        return myService.getToken();
    }

}
