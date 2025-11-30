package com.jfztkg.controller.realtime;

import com.jfztkg.model.User;
import com.jfztkg.service.RtdbUserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rtdb/users")
@RestControllerAdvice
public class RtdbUserController {

    private final RtdbUserService userService;

    public RtdbUserController(RtdbUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{id}")
    public String save(@PathVariable("id") String id, @RequestBody User user) {
        userService.saveUser(id, user);
        return "ok";
    }

    @GetMapping("/{id}")
    public void get(@PathVariable("id") String id) {
        userService.getUserAsync(id, new RtdbUserService.UserCallback() {
            @Override
            public void onUser(User user) {
                System.out.println("Got user: " + user);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @PatchMapping("/{id}")
    public String patch(@PathVariable("id") String id,
                        @RequestBody Map<String, Object> fields) {
        userService.updateUserFields(id, fields);
        return "ok";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return "ok";
    }
}
