package com.santu.leaves.config.dealnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 处理对象类型的null值
 * @Author LEAVES
 * @Date 2020/9/11
 * @Version 1.0
 */

public class MyNullObjectJsonSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeEndObject();
    }
}
