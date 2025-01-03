package cn.oli.crm.jackson.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;

public class EmptyArrayToObjectDeserializer extends JsonDeserializer implements ContextualDeserializer {

    //    @Override
//    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
//        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
//        JsonNode node = mapper.readTree(jsonParser);
//
//        if(!node.isEmpty()){
//            deserializationContext.
//            return mapper.treeToValue(node,);
//        }
//        return null;
//    }
    private JavaType type;

    public EmptyArrayToObjectDeserializer() {
    }

    public EmptyArrayToObjectDeserializer(JavaType type) {
        this.type = type;
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = mapper.readTree(jsonParser);
        if (!node.isEmpty()) {
            return mapper.treeToValue(node, type);
        }
        return null;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        //beanProperty is null when the type to deserialize is the top-level type or a generic type, not a type of a bean property
        JavaType type = deserializationContext.getContextualType() != null
                ? deserializationContext.getContextualType()
                : beanProperty.getMember().getType();
        return new EmptyArrayToObjectDeserializer(type);
    }
}
