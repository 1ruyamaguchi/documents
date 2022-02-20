package com.example.javadbtest.controller;

import com.example.javadbtest.dto.UserDto;
import com.example.javadbtest.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * アプリケーション全体のコントローラー
 * 
 */
@Controller
public class SysController {

    @Autowired
    private UserService userService;

    @ModelAttribute(value = "userDto")
    public UserDto userDto() {
        return new UserDto();
    }

    /** 入力ページ */
    @RequestMapping(value = "/docker-welcome")
    public String welcome() {
        return "page/welcome";
    }

    /** 入力ページから出力ページ */
    @RequestMapping(value = "/docker-result")
    public String result(@Validated @ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model) {

        // 入力内容に不正がある場合は再度入力
        if (result.hasErrors()) {
            return "page/welcome";
        }

        // 入力情報を登録
        userService.insertUser(userDto);
        // テーブルから一覧を取得
        model.addAttribute("userList", userService.selectAll());

        return "page/result";
    }

}
