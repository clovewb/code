package com.santu.leaves.config.dealnull;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 该类控制将null值处理成空集合还是空字符串
 * @Author LEAVES
 * @Date 2020/9/11
 * @Version 1.0
 */
public class MyBeanSerializerModifier extends BeanSerializerModifier {

    //  数组类型
    private JsonSerializer _nullArrayJsonSerializer = new MyNullArrayJsonSerializer();
    // 字符串等类型
    private JsonSerializer _nullJsonSerializer = new MyNullJsonSerializer();
    // 对象类型
    private JsonSerializer _nullObjectSerializer = new MyNullObjectJsonSerializer();



    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List beanProperties) {
        //循环所有的beanPropertyWriter
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = (BeanPropertyWriter) beanProperties.get(i);
            //判断字段的类型，如果是array，list，set则注册nullSerializer
            if (isArrayType(writer)) {
                //给writer注册一个自己的nullSerializer
                writer.assignNullSerializer(this._nullArrayJsonSerializer);
            } else if(isStringOrNumberType(writer)){
                writer.assignNullSerializer(this._nullJsonSerializer);
            }else{
                writer.assignNullSerializer(this._nullObjectSerializer);
            }
        }
        return beanProperties;
    }

    //判断是什么类型
    protected boolean isArrayType(BeanPropertyWriter writer) {
        Class clazz = writer.getPropertyType();
        return clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class);
    }

    //判断是什么类型
    protected boolean isStringOrNumberType(BeanPropertyWriter writer) {
        Class clazz = writer.getPropertyType();
        return clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(int.class) || clazz.equals(Double.class) || clazz.equals(Short.class)
                || clazz.equals(Long.class) || clazz.equals(short.class) || clazz.equals(double.class) || clazz.equals(long.class) || clazz.equals(Date.class)
                || clazz.equals(BigDecimal.class);
    }

}
