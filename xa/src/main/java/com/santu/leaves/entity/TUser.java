package com.santu.leaves.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * t_user
 * @author 
 */
@Data
public class TUser implements Serializable {
    /**
     * 用户ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 角色ID
     */
    private Long roleid;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 头像
     */
    private String icon;

    /**
     * 性别
     */
    private String sex;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 所管理的责任区编码
     */
    private String area;

    private TRole role;

    private static final long serialVersionUID = 1L;
}