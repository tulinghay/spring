package com.huang.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController{
    @RequestMapping("/hello/{a}/{b}")
    public String test(@PathVariable String a, @PathVariable String b, Model model){
        model.addAttribute("msg","helloworld");
        JSONObject object=new JSONObject();

        return "forward:/WEB-INF/jsp/test.jsp";
    }
}
