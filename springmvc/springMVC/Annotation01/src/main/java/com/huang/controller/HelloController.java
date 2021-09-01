package com.huang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController{
    @RequestMapping("/hello")
    public String test( Model model){
        model.addAttribute("msg","helloworld");
        System.out.println("controller");
        return "forward:/WEB-INF/jsp/test.jsp";
    }
}
