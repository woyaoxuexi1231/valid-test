package com.demo.valid.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 演示级联校验：字段上的 {@code @Valid} 才会深入校验 address 内部约束。
 * 若去掉 address 上的 @Valid，city/detail 的注解不会生效。
 */
public class NestedUserRequest {

    @NotBlank(message = "name 不能为空")
    private String name;

    @NotNull(message = "address 不能为 null")
    @Valid
    private AddressDto address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}
