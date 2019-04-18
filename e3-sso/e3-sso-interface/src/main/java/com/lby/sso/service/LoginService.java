package com.lby.sso.service;

import com.lby.common.utils.E3Result;

/**
 * @Author: TSF
 * @Description:登入
 * @Date: Create in 2018/12/23 17:40
 */
public interface LoginService {
    /**
     * 用户登入
     * @param username 用户名
     * @param password 密码
     * @return
     */
    E3Result userLogin(String username, String password);
}
