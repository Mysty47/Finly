package com.example.finly.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
public class HelloService {

    public String print() {
        return "Hello";
    }
}
