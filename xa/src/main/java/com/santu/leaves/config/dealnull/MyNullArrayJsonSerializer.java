package com.santu.leaves.config.dealnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;


/**
 * 处理数组类型的null值
 * @Author LEAVES
 * @Date 2020/9/11
 * @Version 1.0
 */
public class MyNullArrayJsonSerializer extends JsonSerializer {

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value == null) {
            jgen.writeStartArray();
            jgen.writeEndArray();
        }
    }
}