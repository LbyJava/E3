package com.lby.sso.service;

import com.lby.common.utils.E3Result;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/23 23:54
 */
public interface TokenService {
    E3Result getUserByToken(String token);
}
