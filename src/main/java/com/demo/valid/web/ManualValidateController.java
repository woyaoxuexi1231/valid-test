package com.demo.valid.web;

import com.demo.valid.dto.BasicConstraintsRequest;
import com.demo.valid.service.ManualValidateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 实验 4：手动调用 Validator.validate()。
 * 注意：这里参数上故意不加 @Valid，完全靠 Service 里编程式触发。
 */
@RestController
@RequestMapping("/api/manual")
public class ManualValidateController {

    private final ManualValidateService manualValidateService;

    public ManualValidateController(ManualValidateService manualValidateService) {
        this.manualValidateService = manualValidateService;
    }

    @PostMapping
    public Map<String, Object> check(@RequestBody BasicConstraintsRequest request) {
        BasicConstraintsRequest ok = manualValidateService.check(request);
        return Map.of(
                "ok", true,
                "demo", "Validator.validate() 编程式校验",
                "body", ok);
    }
}
