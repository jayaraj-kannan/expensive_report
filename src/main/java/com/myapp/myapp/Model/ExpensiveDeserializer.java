package com.myapp.myapp.Model;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpensiveDeserializer extends JsonDeserializer<ArrayList<Expensive>> {
    @Override
    public ArrayList<Expensive> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ArrayList<Expensive> expensiveList = new ArrayList<Expensive>();
        JsonNode node = p.getCodec().readTree(p);
        if (node.isArray()) {
            for (JsonNode jsonNode : node) {
                System.out.println(jsonNode);
                Expensive expensive = new Expensive();
                expensive.setId(jsonNode.get("id").asLong());
                expensive.setDate(jsonNode.get("date").asText());
                expensive.setDescription(jsonNode.get("description").asText());
                expensive.setExpensiveSource(ExpensiveSource.valueOf(jsonNode.get("expensiveSource").asText()));
                expensive.setExpensiveCategory(ExpensiveCategory.valueOf(jsonNode.get("expensiveCategory").asText()));
                expensive.setAmount(jsonNode.get("amount").asText());
                expensiveList.add(expensive);
            }
        }
        return expensiveList;
    }
}
