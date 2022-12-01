package com.example.javadbtest.service;

import java.util.List;

import com.example.javadbtest.dto.UserDto;
import com.example.javadbtest.entity.User;

import org.springframework.stereotype.Service;

/**
 * ユーザに関するサービスのインターフェース
 * 
 */
@Service
public interface UserService {

    /** ユーザを登録 */
    void insertUser(UserDto userDto);

    /** ユーザを全件取得 */
    List<User> selectAll();
}
