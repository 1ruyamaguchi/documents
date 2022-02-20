package com.example.javadbtest.logic;

import java.util.List;

import com.example.javadbtest.dto.UserDto;
import com.example.javadbtest.entity.User;

import org.springframework.stereotype.Component;

/**
 * ユーザの登録、検索をするロジックインターフェース
 * 
 */
@Component
public interface UserLogic {

    /** ユーザを登録 */
    void insertUser(UserDto userDto);

    /** ユーザを全件取得 */
    List<User> selectAll();
}
