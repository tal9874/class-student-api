package com.example.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Atger
@Controller
public class GuiController {
    @GetMapping(value = "/index.html")
    public String index(){
        return "index";
    }

}
