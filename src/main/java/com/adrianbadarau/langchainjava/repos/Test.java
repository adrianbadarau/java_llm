package com.adrianbadarau.langchainjava.repos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/test")
public class Test {

    @GetMapping("try-gpt")
    void tryGpt(){

    }
}
