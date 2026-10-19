package com.demo.valid.web;

import com.demo.valid.dto.NestedUserRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 实验 2：嵌套（级联）校验。
 * NestedUserRequest.address 上必须有 @Valid，否则 address 内部注解不生效。
 */
@RestController
@RequestMapping("/api/nested")
public class NestedController {

    @PostMapping
    public Map<String, Object> create(@Valid @RequestBody NestedUserRequest request) {
        return Map.of(
                "ok", true,
                "demo", "嵌套对象 @Valid 级联校验",
                "body", request);
    }
}
