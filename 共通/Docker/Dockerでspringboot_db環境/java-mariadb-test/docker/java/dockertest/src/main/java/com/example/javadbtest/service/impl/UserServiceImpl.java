package com.example.javadbtest.service.impl;

import java.util.List;

import com.example.javadbtest.dto.UserDto;
import com.example.javadbtest.entity.User;
import com.example.javadbtest.logic.UserLogic;
import com.example.javadbtest.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ユーザに関するサービスの実装クラス
 * 
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserLogic userLogic;

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public void insertUser(UserDto userDto) {
        // 登録
        userLogic.insertUser(userDto);
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public List<User> selectAll() {
        // ユーザを全件取得
        List<User> userList = userLogic.selectAll();
        return userList;
    }

}
