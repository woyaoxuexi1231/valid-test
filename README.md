# valid-test · Bean Validation 动手实验

把「校验规范 / 注解 / 触发器 / 手动 validate」拆成 5 个可 curl 的接口，对照玩。

仓库：[woyaoxuexi1231/valid-test](https://github.com/woyaoxuexi1231/valid-test)

## 怎么跑

```powershell
cd labs/valid-test   # 若已单独 clone 本仓，则在仓库根目录
mvn spring-boot:run
```

服务：`http://localhost:8099`

## 概念对照（先看这个）

| 你听到的词 | 它到底是什么 |
|-----------|-------------|
| **validation** | Jakarta Bean Validation **规范**（API 包 `jakarta.validation`） |
| **validate()** | `Validator` 接口上的**方法**，手动触发校验 |
| **@Size / @Min / @Max** | 规范里的**标准约束注解** |
| **@Length** | Hibernate Validator **扩展**注解（仅 String） |
| **@Valid** | 规范里的**触发器**：校验 + 支持嵌套级联；不支持 groups |
| **@Validated** | Spring **扩展触发器**：支持分组；类级可开方法参数校验 |

## 五个实验

### 1. 标准约束 `@Size` / `@Length` / `@Min` / `@Max`

`POST /api/basic` —— 参数上 `@Valid`，失败会进统一异常处理。

```powershell
# 故意写错：username 太短、age 超范围、tags 过多
curl -s http://localhost:8099/api/basic -H "Content-Type: application/json" -d "{`"username`":`"ab`",`"password`":`"123456`",`"age`":200,`"tags`":[`"a`",`"b`",`"c`",`"d`",`"e`",`"f`"]}"
```

合法示例：

```powershell
curl -s http://localhost:8099/api/basic -H "Content-Type: application/json" -d "{`"username`":`"alice`",`"password`":`"secret1`",`"age`":20,`"tags`":[`"java`",`"spring`"]}"
```

看代码：`BasicConstraintsRequest`（`@Size` vs `@Length`）、`BasicConstraintsController`。

---

### 2. 嵌套级联 `@Valid`

`POST /api/nested` —— `address` 字段上有 `@Valid` 才会校验内部的 `city` / `detail`。

```powershell
# address.city 为空 → 报错
curl -s http://localhost:8099/api/nested -H "Content-Type: application/json" -d "{`"name`":`"bob`",`"address`":{`"city`":`"`",`"detail`":`"road`"}}"
```

自己试：去掉 `NestedUserRequest.address` 上的 `@Valid` 再请求，会发现内部注解不生效。

---

### 3. 分组校验 `@Validated(Group)`

同一 DTO，创建 / 更新规则不同。**只能用 `@Validated`，标准 `@Valid` 不能指定 groups。**

```powershell
# 创建：带了 id → 失败（CreateGroup 要求 id 为 null）
curl -s http://localhost:8099/api/grouped -H "Content-Type: application/json" -d "{`"id`":1,`"username`":`"alice`",`"password`":`"secret1`"}"

# 创建：正确
curl -s http://localhost:8099/api/grouped -H "Content-Type: application/json" -d "{`"username`":`"alice`",`"password`":`"secret1`"}"

# 更新：缺 id → 失败
curl -s -X PUT http://localhost:8099/api/grouped -H "Content-Type: application/json" -d "{`"username`":`"alice`"}"

# 更新：正确
curl -s -X PUT http://localhost:8099/api/grouped -H "Content-Type: application/json" -d "{`"id`":1,`"username`":`"alice`",`"password`":`"secret1`"}"
```

看代码：`GroupedUserRequest` + `CreateGroup` / `UpdateGroup` + `GroupedController`。

---

### 4. 手动 `validator.validate()`

`POST /api/manual` —— Controller **不加** `@Valid`，在 Service 里编程式校验。

```powershell
curl -s http://localhost:8099/api/manual -H "Content-Type: application/json" -d "{`"username`":`"x`",`"password`":`"123`",`"age`":0,`"tags`":[]}"
```

看代码：`ManualValidateService`。这就是「validation 规范」里的核心动作方法。

---

### 5. 方法参数 / Path / Query 校验

需要类上有 `@Validated`（本实验在 `PathParamController` 上已标）。

```powershell
# id 超范围
curl -s "http://localhost:8099/api/params/users/0?nickname=a"

# 合法
curl -s "http://localhost:8099/api/params/users/42?nickname=neo"
```

## 目录

```text
src/main/java/com/demo/valid/
├── ValidTestApplication.java      # 启动类（类上 @Validated）
├── dto/                           # 带约束注解的请求体
├── group/                         # CreateGroup / UpdateGroup
├── service/ManualValidateService  # validate() 手动触发
└── web/                           # 5 组 Controller + 异常转 JSON
```

## 建议玩法

1. 先打挂 `/api/basic`，看 JSON 里 `field` / `message`。
2. 改注解边界（比如 `@Size(min=5)`），重启再打，确认语义。
3. 对比 `/api/basic`（注解自动）和 `/api/manual`（手写 validate）。
4. 在分组接口上来回切换 Create / Update，体会为什么需要 `@Validated`。
