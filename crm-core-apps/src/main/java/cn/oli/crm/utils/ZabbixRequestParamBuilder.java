package cn.oli.crm.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.ObjectUtils;

/**
 * @author daixiaoyi
 */
public class ZabbixRequestParamBuilder {
    private final JsonNode params;

    private String nodeType = "object";

    public ZabbixRequestParamBuilder() {
        params = JsonUtil.newObjectNode();
    }

    //支持array和object两种类型
    public ZabbixRequestParamBuilder(String nodeType) {
        this.nodeType = nodeType;
        switch (nodeType) {
            case "array":
                params = JsonUtil.newArrayNode();
                break;
            case "object":
                params = JsonUtil.newObjectNode();
                break;
            default:
                throw new RuntimeException("nodeType must be 'array' or 'object'");
        }
    }

    public ZabbixRequestParamBuilder addParam(Object paramValue) {
        if("array".equals(nodeType)) {
            ((ArrayNode) params).addPOJO(paramValue);
        }
        return this;
    }

    public ZabbixRequestParamBuilder addParam(String paramName, Object paramValue) {
        if("object".equals(nodeType)) {
            ((ObjectNode) params).putPOJO(paramName, paramValue);
        }
        return this;
    }

    public ZabbixRequestParamBuilder addParamIfNotEmpty(String paramName, Object paramValue) {
        if("object".equals(nodeType)) {
            if (ObjectUtils.isNotEmpty(paramValue)) {
                ((ObjectNode) params).putPOJO(paramName, paramValue);
            }
        }
        return this;
    }

    public JsonNode build() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(params.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
