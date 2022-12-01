package com.example.javadbtest.logic.impl;

import java.util.ArrayList;
import java.util.List;

import com.example.javadbtest.dto.UserDto;
import com.example.javadbtest.entity.User;
import com.example.javadbtest.logic.UserLogic;
import com.example.javadbtest.mapper.UserMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLogicImpl implements UserLogic {

    @Autowired
    private UserMapper userMapper;

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public void insertUser(UserDto userDto) {
        // 入力情報をエンティティに詰め替える
        User user = new User();
        BeanUtils.copyProperties(userDto, user);

        // 登録を実行
        userMapper.insertUser(user);
    }

    @Override
    public List<UserDto> selectAll() {
        // 返却値を定義
        List<UserDto> userDtoList = new ArrayList<>();
        // DBから全件取得
        List<User> userList = userMapper.selectAll();

        // 詰め替え
        for (User user : userList) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            userDtoList.add(userDto);
        }

        return userDtoList;
    }

}
