package com.santu.leaves.config.basemapper;


import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 通用基础mapper
 * @param <T>
 *
 * @Author LEAVES
 * @Date 2020/8/31
 * @Version 1.0
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
