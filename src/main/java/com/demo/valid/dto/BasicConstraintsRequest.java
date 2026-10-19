package com.demo.valid.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

/**
 * 对照标准注解 vs Hibernate 扩展注解。
 * <ul>
 *   <li>{@code @Size} — Bean Validation 标准，可用于 String / Collection / Map / Array</li>
 *   <li>{@code @Length} — Hibernate Validator 扩展，仅 String</li>
 *   <li>{@code @Min} / {@code @Max} — Bean Validation 标准，用于数字</li>
 * </ul>
 */
public class BasicConstraintsRequest {

    @NotBlank(message = "username 不能为空")
    @Size(min = 3, max = 16, message = "username 长度必须在 3~16（@Size）")
    private String username;

    /**
     * 故意同时标 @Size 和 @Length：对 String 语义重叠。
     * 实际项目只保留其一，优先 @Size（标准）。
     */
    @Length(min = 6, max = 32, message = "password 长度必须在 6~32（@Length）")
    private String password;

    @Min(value = 1, message = "age 最小为 1")
    @Max(value = 150, message = "age 最大为 150")
    private Integer age;

    @Size(min = 1, max = 5, message = "tags 数量必须在 1~5")
    private java.util.List<String> tags;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public java.util.List<String> getTags() {
        return tags;
    }

    public void setTags(java.util.List<String> tags) {
        this.tags = tags;
    }
}
