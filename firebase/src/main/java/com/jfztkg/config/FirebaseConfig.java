package com.jfztkg.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials.path}")
    private String firebaseConfigPath;

    @Value("${firebase.project-id}")
    private String projectId;

    // 🔴 新增：Realtime Database 的 URL
    @Value("${firebase.database-url}")
    private String databaseUrl;

    private final ResourceLoader resourceLoader;

    public FirebaseConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 初始化 FirebaseApp（全局只一个）
     */
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream serviceAccount;

        // 支持 classpath:xxx 和 绝对文件路径 两种写法
        if (firebaseConfigPath.startsWith("classpath:")) {
            Resource resource = resourceLoader.getResource(firebaseConfigPath);
            serviceAccount = resource.getInputStream();
        } else {
            Resource resource = resourceLoader.getResource("file:" + firebaseConfigPath);
            serviceAccount = resource.getInputStream();
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId(projectId)
                // 🔴 关键：一定要加上 databaseUrl
                .setDatabaseUrl(databaseUrl)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            // 已经初始化过直接复用
            return FirebaseApp.getInstance();
        }
    }

    /**
     * Firestore Bean（文档库）
     */
    @Bean
    public Firestore firestore(FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore(firebaseApp);
    }

    /**
     * Realtime Database Bean（可选）
     */
    @Bean
    public DatabaseReference realtimeDatabase(FirebaseApp firebaseApp) {
        // 这里用 firebaseApp 就可以了，URL 已经在 FirebaseOptions 里设过
        return FirebaseDatabase.getInstance(firebaseApp).getReference();
        // 如果你想固定某个根路径，也可以：
        // return FirebaseDatabase.getInstance(firebaseApp).getReference("users");
    }
}
