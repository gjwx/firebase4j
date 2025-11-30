package com.jfztkg.service;

/**
 * @Author gongjiawei
 * @Date 2025/11/28 23:28
 * @注释
 */
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.jfztkg.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class FirestoreUserService {

    private static final String COLLECTION_NAME = "users";

    private final Firestore firestore;

    public FirestoreUserService(Firestore firestore) {
        this.firestore = firestore;
    }

    // ========= Create =========

    /** 使用指定 ID 创建 / 覆盖文档 */
    public void createOrUpdateUser(String userId, User user)
            throws ExecutionException, InterruptedException {

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", user.getName());
        data.put("age", user.getAge());
        data.put("active", user.getActive());

        ApiFuture<WriteResult> future =
                firestore.collection(COLLECTION_NAME)
                        .document(userId)
                        .set(data);

        WriteResult result = future.get();
        System.out.println("Saved at: " + result.getUpdateTime());
    }

    /** 让 Firestore 自动生成 ID */
    public String createUserAutoId(User user)
            throws ExecutionException, InterruptedException {

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", user.getName());
        data.put("age", user.getAge());
        data.put("active", user.getActive());

        ApiFuture<DocumentReference> future =
                firestore.collection(COLLECTION_NAME)
                        .add(data);

        DocumentReference ref = future.get();
        System.out.println("New user id = " + ref.getId());
        return ref.getId();
    }

    // ========= Read =========

    /** 读取单个用户 */
    public User getUser(String userId)
            throws ExecutionException, InterruptedException {

        DocumentReference docRef =
                firestore.collection(COLLECTION_NAME).document(userId);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot snapshot = future.get();

        if (!snapshot.exists()) {
            return null;
        }

        User user = new User();
        user.setId(snapshot.getId());
        user.setName(snapshot.getString("name"));
        Long ageLong = snapshot.getLong("age");
        user.setAge(ageLong == null ? null : ageLong.intValue());
        Boolean active = snapshot.getBoolean("active");
        user.setActive(active);

        return user;
    }

    /** 查询所有 active = true 的用户 */
    public List<User> listActiveUsers()
            throws ExecutionException, InterruptedException {

        CollectionReference usersCollection =
                firestore.collection(COLLECTION_NAME);

        ApiFuture<QuerySnapshot> future =
                usersCollection.whereEqualTo("active", true).get();

        QuerySnapshot snapshot = future.get();
        List<QueryDocumentSnapshot> docs = snapshot.getDocuments();

        List<User> result = new ArrayList<User>();
        for (DocumentSnapshot d : docs) {
            User u = new User();
            u.setId(d.getId());
            u.setName(d.getString("name"));
            Long ageLong = d.getLong("age");
            u.setAge(ageLong == null ? null : ageLong.intValue());
            u.setActive(d.getBoolean("active"));
            result.add(u);
        }
        return result;
    }

    // ========= Update =========

    /** 部分字段更新（不覆盖整个文档） */
    public void updateUserFields(String userId, Map<String, Object> fieldsToUpdate)
            throws ExecutionException, InterruptedException {

        DocumentReference docRef =
                firestore.collection(COLLECTION_NAME).document(userId);

        ApiFuture<WriteResult> future = docRef.update(fieldsToUpdate);
        WriteResult result = future.get();
        System.out.println("Updated at: " + result.getUpdateTime());
    }

    // ========= Delete =========

    /** 删除整个文档 */
    public void deleteUser(String userId)
            throws ExecutionException, InterruptedException {

        DocumentReference docRef =
                firestore.collection(COLLECTION_NAME).document(userId);

        ApiFuture<WriteResult> future = docRef.delete();
        future.get();
        System.out.println("Deleted user: " + userId);
    }
}

