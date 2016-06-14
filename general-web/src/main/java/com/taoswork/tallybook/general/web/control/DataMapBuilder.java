package com.taoswork.tallybook.general.web.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gao Yuan on 2016/3/30.
 */
public class DataMapBuilder {
    private final Map<String, Object> innerMap = new HashMap<String, Object>();
    private final String attributeName;
    private final ObjectMapper objectMapper;

    public DataMapBuilder(String attributeName, ObjectMapper objectMapper) {
        this.attributeName = attributeName;
        this.objectMapper = objectMapper;
    }

    public DataMapBuilder addAttribute(String attributeName, Object attributeValue){
        return addAttribute(attributeName, attributeValue, true);
    }

    public DataMapBuilder addAttribute(String attributeName, Object attributeValue, boolean condition){
        if(condition){
            innerMap.put(attributeName, attributeValue);
        }
        return this;
    }

    public void addToModule(Model model){
        model.addAttribute(attributeName, getResultInJson());
    }

    public String getResultInJson(){
        try {
            return objectMapper.writeValueAsString(innerMap);
        } catch (JsonProcessingException exp) {
            throw new RuntimeException(exp);
        }
    }
}
