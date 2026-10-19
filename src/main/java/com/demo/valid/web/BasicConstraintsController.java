package com.demo.valid.web;

import com.demo.valid.dto.BasicConstraintsRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 实验 1：标准约束注解触发方式——参数上的 {@code @Valid}。
 * POST /api/basic
 */
@RestController
@RequestMapping("/api/basic")
public class BasicConstraintsController {

    @PostMapping
    public Map<String, Object> create(@Valid @RequestBody BasicConstraintsRequest request) {
        return Map.of(
                "ok", true,
                "demo", "@Valid + @Size/@Length/@Min/@Max",
                "body", request);
    }
}
