package com.zqhyz.vo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
public class UserVo implements Serializable {
    private String id;

    private String username;

    private String name;

//    private String password;

    private String avatar;

    private boolean enable;

    Collection<? extends GrantedAuthority> authorities;

    List<String> roles;

    List<String> permissions;
}
