package com.tele.utils;

import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @author zhangleimin
 * @package com.tele.utils
 * @date 16-9-29
 */
@Log4j
public class ParamValidate {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    /**
     * 请求参数基本校验
     * @param validateModel 请求参数对象
     */
    public static <T> void validateParams(T validateModel){
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(validateModel);
        if (violations.size() == 0) {
            return ;
        }
        StringBuilder buf = new StringBuilder();
        for (ConstraintViolation<T> violation : violations) {
            log.debug("参数错误:" + violation.getMessage());
            buf.append(violation.getMessage()).append("$");
        }
        if(violations.size() == 1 && StringUtils.isNotEmpty(buf.toString())){
            buf.deleteCharAt( buf.length() -1 );
        }
        throw new IllegalArgumentException(buf.toString());
    }
}
