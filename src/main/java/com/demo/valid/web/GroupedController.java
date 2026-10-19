package com.demo.valid.web;

import com.demo.valid.dto.GroupedUserRequest;
import com.demo.valid.group.CreateGroup;
import com.demo.valid.group.UpdateGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 实验 3：分组校验——只能用 Spring 的 {@code @Validated(Group.class)}，
 * 标准 {@code @Valid} 不支持指定 groups。
 */
@RestController
@RequestMapping("/api/grouped")
public class GroupedController {

    @PostMapping
    public Map<String, Object> create(
            @Validated(CreateGroup.class) @RequestBody GroupedUserRequest request) {
        return Map.of(
                "ok", true,
                "demo", "@Validated(CreateGroup) — 创建：id 必须空，username 必填",
                "body", request);
    }

    @PutMapping
    public Map<String, Object> update(
            @Validated(UpdateGroup.class) @RequestBody GroupedUserRequest request) {
        return Map.of(
                "ok", true,
                "demo", "@Validated(UpdateGroup) — 更新：id 必填",
                "body", request);
    }
}
