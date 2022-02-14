package com.example.javadbtest.mapper;

import java.util.List;

import com.example.javadbtest.entity.User;

import org.springframework.stereotype.Component;

/**
 * userテーブルのmapperクラス
 * 
 */
@Component
public interface UserMapper {

    /** ユーザを登録する */
    void insertUser(User user);

    /** ユーザを全件取得する */
    List<User> selectAll();
}
