package com;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

@org.springframework.stereotype.Controller
@RequestMapping("hello")
public class HelloController implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String method=httpServletRequest.getParameter("method");
        ModelAndView mv=new ModelAndView();
        mv.addObject("msg","msg");
        mv.setViewName("hello");
        return mv;
    }
}




