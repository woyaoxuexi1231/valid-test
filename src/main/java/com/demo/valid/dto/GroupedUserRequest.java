package com.demo.valid.dto;

import com.demo.valid.group.CreateGroup;
import com.demo.valid.group.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

/**
 * 分组校验：同一 DTO，创建 / 更新用不同规则。
 * <ul>
 *   <li>创建：id 必须为 null，username 必填</li>
 *   <li>更新：id 必填，username 可选（若传则校验长度）</li>
 * </ul>
 */
public class GroupedUserRequest {

    @Null(groups = CreateGroup.class, message = "创建时 id 必须为空")
    @NotNull(groups = UpdateGroup.class, message = "更新时 id 不能为空")
    private Long id;

    @NotBlank(groups = CreateGroup.class, message = "创建时 username 不能为空")
    @Size(min = 3, max = 16, groups = {CreateGroup.class, UpdateGroup.class},
            message = "username 长度必须在 3~16")
    private String username;

    @Size(min = 6, max = 32, groups = {CreateGroup.class, UpdateGroup.class},
            message = "password 长度必须在 6~32")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
