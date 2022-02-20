package com.example.javadbtest.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * ユーザに関するDto
 * 
 */
@Data
public class UserDto {
    /** 名前 */
    @NotEmpty(message = "{0}を入力してください")
    private String userName;

    /** 説明 */
    private String detail;
}
