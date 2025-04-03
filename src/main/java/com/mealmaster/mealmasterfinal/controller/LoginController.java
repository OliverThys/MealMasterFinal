package com.mealmaster.mealmasterfinal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "success", required = false) String success, Model model) {
    if (success != null) {
        model.addAttribute("message", "Account created successfully! You can now log in.");
    }
    return "login";
}

}
