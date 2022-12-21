package com.lengao.auth.controller;

import com.lengao.auth.config.BusinessException;
import com.lengao.auth.po.BmgUser;
import com.lengao.auth.service.IBmgUserService;
import com.lengao.auth.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.headers.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户 角色映射表 前端控制器
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-18
 */

@RestController
@RequestMapping("/auth")
@Api("登录注册")

public class LoginController {

    @Autowired
    IBmgUserService bmgUserService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result<Boolean> login(BmgUser user,HttpServletResponse response) {
        try {
            bmgUserService.login(user, response);
            return Result.ofSuccess();
        } catch (Exception e) {
            return Result.ofFail();
        }
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody BmgUser user) {
        int count = bmgUserService.register(user);
        return Result.ofSuccess(count > 0);
    }

    @ApiOperation("test")
    @GetMapping("/test")
    public String test(@RequestHeader String token) {
        return "12312";
    }
}
