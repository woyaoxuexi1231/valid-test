package com.demo.valid.service;

import com.demo.valid.dto.BasicConstraintsRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 演示编程式校验：不依赖 Controller 注解，直接调 {@code validator.validate(obj)}。
 * 这就是「validation 规范」里的核心动作方法 {@code validate()}。
 */
@Service
public class ManualValidateService {

    private final Validator validator;

    public ManualValidateService(Validator validator) {
        this.validator = validator;
    }

    public BasicConstraintsRequest check(BasicConstraintsRequest request) {
        Set<ConstraintViolation<BasicConstraintsRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return request;
    }
}
