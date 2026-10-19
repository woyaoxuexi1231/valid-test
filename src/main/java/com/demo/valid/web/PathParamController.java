package com.demo.valid.web;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 实验 5：方法参数 / Query / Path 校验。
 * 需要类上（或启动类上）有 {@code @Validated}，Spring 才会代理并校验方法参数。
 */
@Validated
@RestController
@RequestMapping("/api/params")
public class PathParamController {

    @GetMapping("/users/{id}")
    public Map<String, Object> byId(
            @PathVariable @Min(1) @Max(9999) Long id,
            @RequestParam(required = false, defaultValue = "guest")
            @Size(min = 2, max = 20) String nickname) {
        return Map.of(
                "ok", true,
                "demo", "方法参数校验（需类级 @Validated）",
                "id", id,
                "nickname", nickname);
    }
}
