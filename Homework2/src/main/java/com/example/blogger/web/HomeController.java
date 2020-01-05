package com.example.blogger.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("path","login");
        return "login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("path","login");
        return "login";
    }
}
