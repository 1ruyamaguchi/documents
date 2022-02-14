package com.example.javadbtest.controller;

import com.example.javadbtest.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/dockertest")
    public String welcome(Model model) {

        // 登録
        userService.insertUser();
        // 取得
        model.addAttribute("userList", userService.selectAll());

        return "page/welcome";
    }

}
