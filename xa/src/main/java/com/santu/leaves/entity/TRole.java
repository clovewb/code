package com.santu.leaves.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * t_role
 * @author 
 */
@Data
public class TRole implements Serializable {
    /**
     * 角色ID 
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 角色编号
     */
    private String name;

    /**
     * 权限ID
     */
    private String auths;

    /**
     * 父级角色ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentid;

    private static final long serialVersionUID = 1L;
}