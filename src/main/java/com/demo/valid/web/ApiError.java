package com.demo.valid.web;

import java.util.List;

/** 统一错误响应，方便 curl 对照看字段级错误 */
public record ApiError(String message, List<FieldErrorDetail> errors) {

    public record FieldErrorDetail(String field, String rejectedValue, String message) {
    }
}
