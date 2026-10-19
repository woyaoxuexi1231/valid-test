package com.demo.valid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** 嵌套对象：只有外层字段标了 @Valid，这些约束才会被级联触发 */
public class AddressDto {

    @NotBlank(message = "city 不能为空")
    private String city;

    @Size(min = 5, max = 64, message = "detail 长度必须在 5~64")
    private String detail;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
