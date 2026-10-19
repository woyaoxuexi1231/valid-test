/**
 * 第 1 层：规则（和 Spring 无关）。
 * <p>
 * 看 import：{@code jakarta.validation.constraints} = Java 标准；
 * {@code org.hibernate.validator.constraints} = Hibernate 扩展（如 @Length）。
 * 这些类只描述「什么叫合法」，不会自己执行。
 */
package com.demo.valid.dto;
