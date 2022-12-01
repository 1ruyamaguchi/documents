package com.example.javadbtest.dto;

import lombok.Data;

/**
 * ユーザに関するDto
 * 
 */
@Data
public class UserDto {
    /** ID */
    private Integer id;

    /** 名前 */
    private String userName;

    /** 説明 */
    private String detail;
}
