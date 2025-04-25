package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.json.JsonMapper;

import java.lang.reflect.Type;

// ********************************
// ********************************
// ********************************
// THIS CLASS IS MADE WITH CHATGPT!
// ********************************
// ********************************
// ********************************

// Purpose: more appealing date formats

public class CustomJacksonMapper implements JsonMapper {
    private final ObjectMapper mapper;

    public CustomJacksonMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String toJsonString(Object obj, Type type) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON serialization failed", e);
        }
    }

    @Override
    public <T> T fromJsonString(String json, Type targetType) {
        try {
            return mapper.readValue(json, mapper.constructType(targetType));
        } catch (Exception e) {
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }
}
