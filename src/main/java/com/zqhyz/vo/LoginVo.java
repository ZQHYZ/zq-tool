package com.zqhyz.vo;

import com.zqhyz.entity.User;
import lombok.Data;

import java.io.Serializable;
@Data
public class LoginVo implements Serializable {
    private User user;
    private String token;
}
