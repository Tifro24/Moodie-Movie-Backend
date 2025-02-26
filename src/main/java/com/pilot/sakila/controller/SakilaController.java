package com.pilot.sakila.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class SakilaController {

    @GetMapping("/greeting/{name}")
    public String getGreeting(@PathVariable String name){
        return String.format("Hello, %s", name);
    }

    @GetMapping("/hello")
    public String getDefault(){
        return "Hello World!";
    }
}
