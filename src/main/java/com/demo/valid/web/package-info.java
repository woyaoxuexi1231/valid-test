/**
 * 第 3 层：Spring 触发方式。
 * <ul>
 *   <li>{@code jakarta.validation.Valid}：标准触发器，Spring MVC 在绑完请求体后替你调用</li>
 *   <li>{@code org.springframework.validation.annotation.Validated}：Spring 扩展，能传分组、能代理方法参数</li>
 *   <li>{@code ValidationExceptionHandler}：把校验失败变成 HTTP 400，这是 Spring 的壳，不是规范</li>
 * </ul>
 */
package com.demo.valid.web;
