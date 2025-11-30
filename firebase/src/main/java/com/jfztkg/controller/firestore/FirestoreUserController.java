package com.jfztkg.controller.firestore;

import com.jfztkg.common.ApiResult;
import com.jfztkg.common.ResultCode;
import com.jfztkg.model.User;
import com.jfztkg.service.FirestoreUserService;
import org.springframework.web.bind.annotation.*;

/**
 * @Author gongjiawei
 * @Date 2025/11/28 23:21
 * @注释
 */
@RestController
@RequestMapping("/firestore/users")
public class FirestoreUserController {

    private final FirestoreUserService userService;

    public FirestoreUserController(FirestoreUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ApiResult<User> getUser(@PathVariable("id") String id) {
        try {
            User user = userService.getUser(id);
            if (user == null) {
                return ApiResult.failure(ResultCode.NOT_FOUND, "用户不存在：" + id);
            }
            return ApiResult.success(user);
        } catch (Exception e) {
            // 可以加日志
            e.printStackTrace();
            return ApiResult.failure(ResultCode.SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/{id}")
    public ApiResult<Void> saveUser(@PathVariable("id") String id,
                                    @RequestBody User user) {
        try {
            userService.createOrUpdateUser(id, user);
            return ApiResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.failure(ResultCode.SERVER_ERROR, e.getMessage());
        }
    }
}

