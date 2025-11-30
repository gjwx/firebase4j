package com.jfztkg.service;

/**
 * @Author gongjiawei
 * @Date 2025/11/28 23:32
 * @注释
 */
import com.google.firebase.database.*;
import com.jfztkg.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RtdbUserService {

    // 指向根节点引用
    private final DatabaseReference rootRef;

    public RtdbUserService(DatabaseReference rootRef) {
        this.rootRef = rootRef;
    }

    private DatabaseReference usersRef() {
        return rootRef.child("users");
    }

    // ========= Create / Update =========

    /**
     * 创建或覆盖一个用户节点：/users/{userId}
     * Realtime DB 中 setValueAsync 相当于 “整棵子树覆盖”
     */
    public void saveUser(String userId, User user) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", user.getName());
        data.put("age", user.getAge());
        data.put("active", user.getActive());

        usersRef().child(userId).setValueAsync(data);
    }

    /**
     * 部分字段更新，对应 updateChildrenAsync
     */
    public void updateUserFields(String userId, Map<String, Object> fieldsToUpdate) {
        usersRef().child(userId).updateChildrenAsync(fieldsToUpdate);
    }

    // ========= Read =========

    /**
     * 读取单个用户（异步回调）
     * 注意：Java Admin SDK 对 Realtime DB 是基于监听器的异步模式。
     */
    public void getUserAsync(String userId, final UserCallback callback) {
        usersRef().child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            callback.onUser(null);
                            return;
                        }
                        // 将 DataSnapshot 转为 User
                        Map value = (Map) snapshot.getValue();
                        if (value == null) {
                            callback.onUser(null);
                            return;
                        }
                        User user = new User();
                        user.setId(snapshot.getKey());
                        Object name = value.get("name");
                        user.setName(name == null ? null : String.valueOf(name));

                        Object ageObj = value.get("age");
                        if (ageObj instanceof Number) {
                            user.setAge(((Number) ageObj).intValue());
                        }

                        Object activeObj = value.get("active");
                        if (activeObj instanceof Boolean) {
                            user.setActive((Boolean) activeObj);
                        }

                        callback.onUser(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Read failed: " + error.getMessage());
                        callback.onError(error.toException());
                    }
                });
    }

    // ========= Delete =========

    /** 删除节点：/users/{userId} */
    public void deleteUser(String userId) {
        usersRef().child(userId).removeValueAsync();
    }

    // ========= 辅助回调接口 =========

    public interface UserCallback {
        void onUser(User user);

        void onError(Exception e);
    }
}

