package com.example.javadbtest.entity;

import lombok.Data;

/**
 * userテーブルのエンティティクラス
 * 
 */
@Data
public class User {
    /** ID */
    private Integer id;

    /** 名前 */
    private String userName;

    /** 説明 */
    private String detail;
}
