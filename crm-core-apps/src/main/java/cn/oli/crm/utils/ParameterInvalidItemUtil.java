package cn.oli.crm.utils;

import cn.oli.crm.model.dto.ParameterInvalidItem;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 参数非法项工具类
 *
 * @author csj
 */
public class ParameterInvalidItemUtil {

    public static List<ParameterInvalidItem> convertCVSetToParameterInvalidItemList(Set<ConstraintViolation<?>> cvset) {
        if (CollectionUtils.isEmpty(cvset)) {
            return null;
        }
        return cvset.stream().map(cv -> {
            ParameterInvalidItem parameterInvalidItem = new ParameterInvalidItem();
            String propertyPath = cv.getPropertyPath().toString();
            if (propertyPath.contains(".")) {
                String[] propertyPathArr = propertyPath.split("\\.");
                parameterInvalidItem.setFieldName(propertyPathArr[propertyPathArr.length - 1]);
            } else {
                parameterInvalidItem.setFieldName(propertyPath);
            }
            parameterInvalidItem.setMessage(cv.getMessage());
            return parameterInvalidItem;
        }).collect(Collectors.toList());
    }

    public static List<ParameterInvalidItem> convertBindingResultToParameterInvalidItemList(BindingResult bindingResult) {
        if (bindingResult == null) {
            return null;
        }
        return bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> ParameterInvalidItem.builder().fieldName(fieldError.getField()).message(fieldError.getDefaultMessage()).build())
                .collect(Collectors.toList());
    }
}
