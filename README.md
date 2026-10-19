# valid-test · 按文件读，就能看出三层

仓库：[woyaoxuexi1231/valid-test](https://github.com/woyaoxuexi1231/valid-test)

这个项目不是「五个互不相关的接口」。它是同一套规则的三种触发方式。打开代码时只看两件事：

1. **文件在哪个包**（`dto` / `service` / `web`）
2. **import 是哪个包名**

## 三层分别在哪

```text
dto/     规则        jakarta.validation.constraints     Java 标准注解
                     org.hibernate.validator.constraints Hibernate 扩展（只有 @Length）
service/ 手动触发    jakarta.validation.Validator        标准 API，自己调 validate()
web/     框架触发    jakarta.validation.Valid            标准触发器，Spring MVC 帮你调
                     org.springframework...Validated     Spring 多出来的（分组、方法参数）
```

不用 Spring 时：保留 `dto` 的注解，在边界自己写 `service` 里那一行 `validate()`。
切面不是必须。只有想「方法一进来就自动拦」时，才需要 Spring 的 `@Validated`（或你自己写代理）。

## 按这个顺序打开文件

### 1. `pom.xml`

`spring-boot-starter-validation` 拉进来的是两样东西：规范 API + Hibernate Validator 实现。
Spring 没有另造一套 `@Size`。

### 2. `dto/BasicConstraintsRequest.java`（规则）

看文件头的 import：

- `jakarta.validation.constraints.Size / Min / Max` → 标准
- `org.hibernate.validator.constraints.Length` → 扩展，只给 String 用

`dto/AddressDto.java`、`dto/NestedUserRequest.java`、`dto/GroupedUserRequest.java`、`group/*` 同一层。
`NestedUserRequest.address` 上的 `@Valid` 是标准的「级联开关」：去掉它，里面的 city/detail 不会被查。

### 3. `service/ManualValidateService.java`（标准触发，无切面）

```java
validator.validate(request);
```

这就是规范里的动作。对应接口 `POST /api/manual`：
`web/ManualValidateController` 上**没有** `@Valid`，所以校验不是 Spring 自动做的。

### 4. `web/BasicConstraintsController.java`（Spring 替你触发，仍用标准 @Valid）

```java
create(@Valid @RequestBody BasicConstraintsRequest request)
```

- `@Valid` 的 import 是 `jakarta.validation.Valid`（标准）
- `@RequestBody` / `@RestController` 是 Spring

Spring MVC 在 JSON 绑成对象之后调用校验。你没写切面。

### 5. `web/GroupedController.java`（Spring 多出来的：分组）

import 变成 `org.springframework.validation.annotation.Validated`。

```java
@Validated(CreateGroup.class)
```

标准 `@Valid` 写不出括号里的分组。分组本身（`CreateGroup`）仍是普通空接口，属于标准的 groups 机制。

### 6. `web/PathParamController.java`（Spring 的方法代理）

类上有 `@Validated`，参数上才有 `@Min` / `@Size`。
这是「框架用代理拦方法」。标准只定义 `@Min`，不会在你进方法前拦住。

### 7. `web/ValidationExceptionHandler.java`（Spring 把结果变成 HTTP）

同一份失败，两条路异常类型不同：

| 你打的接口 | 谁触发的 | 异常类型 | JSON 里的 message |
|-----------|----------|----------|-------------------|
| `POST /api/basic` | Spring + `@Valid` | `MethodArgumentNotValidException` | 请求体校验失败 |
| `POST /api/manual` | 你写的 `validate()` | `ConstraintViolationException` | 约束校验失败 |

规则是同一份 `BasicConstraintsRequest`。message 不同，说明触发层不同。

## 怎么跑、怎么对照

```powershell
cd labs/valid-test
mvn spring-boot:run
```

服务：`http://localhost:8099`

同一份坏数据打两次，看返回的 `message`：

```powershell
curl -s http://localhost:8099/api/basic -H "Content-Type: application/json" -d "{`"username`":`"ab`",`"password`":`"123`",`"age`":200,`"tags`":[]}"
curl -s http://localhost:8099/api/manual -H "Content-Type: application/json" -d "{`"username`":`"ab`",`"password`":`"123`",`"age`":200,`"tags`":[]}"
```

其它接口（确认分层之后再玩）：

```powershell
# 嵌套：address.city 空。看 NestedUserRequest 字段上的 @Valid
curl -s http://localhost:8099/api/nested -H "Content-Type: application/json" -d "{`"name`":`"bob`",`"address`":{`"city`":`"`",`"detail`":`"road`"}}"

# 分组：创建时带 id 会失败。看 GroupedController 的 @Validated(CreateGroup)
curl -s http://localhost:8099/api/grouped -H "Content-Type: application/json" -d "{`"id`":1,`"username`":`"alice`",`"password`":`"secret1`"}"

# 方法参数：id=0 失败。看 PathParamController 类上的 @Validated
curl -s "http://localhost:8099/api/params/users/0?nickname=a"
```
