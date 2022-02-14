package com.example.javadbtest.service;

import java.util.List;

import com.example.javadbtest.dto.UserDto;
import com.example.javadbtest.logic.UserLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserLogic userLogic;

    /**
     * ユーザを登録
     * 
     */
    public void insertUser() {
        // 登録内容を定義
        UserDto userDto = new UserDto();
        userDto.setUserName("test_user");
        userDto.setDetail("Docker用のテストユーザです。");

        // 登録
        userLogic.insertUser(userDto);
        // 正常終了を出力
        System.out.println("ユーザの登録が正常に終了しました。");
    }

    /**
     * ユーザを全件取得
     * 
     */
    public List<UserDto> selectAll() {
        // ユーザを全件取得
        List<UserDto> userDtoList = userLogic.selectAll();
        // 取得したユーザを出力
        for (UserDto user : userDtoList) {
            System.out.println(user);
        }
        return userDtoList;
    }

}
