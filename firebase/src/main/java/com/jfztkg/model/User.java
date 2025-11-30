package com.jfztkg.model;

import lombok.Data;

/**
 * @Author gongjiawei
 * @Date 2025/11/28 23:26
 * @注释
 */
@Data
public class User {

    private String id;
    private String name;
    private Integer age;
    private Boolean active;

    public User() {
    }
    public User(String id, String name, Integer age, Boolean active) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.active = active;
    }

    // getter / setter 省略
}

