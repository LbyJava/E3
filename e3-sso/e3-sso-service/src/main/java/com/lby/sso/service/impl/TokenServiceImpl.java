package com.lby.sso.service.impl;

import com.lby.common.jedis.JedisClient;
import com.lby.common.utils.E3Result;
import com.lby.common.utils.JsonUtils;
import com.lby.pojo.TbUser;
import com.lby.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: TSF
 * @Description:根据Token取用户信息
 * @Date: Create in 2018/12/23 23:57
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public E3Result getUserByToken(String token) {
        //根据token到redis中取用户信息
        String json = jedisClient.get("SESSION:" + token);
        //取不到用户信息，登录已经过期，返回登入过期
        if (StringUtils.isBlank(json)) {
            return E3Result.build(201, "用户登入已经过期");
        }
        //取到用户信息更新token的过期时间
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        //返回结果，のresult其中包含TbUser对象
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return E3Result.ok(user);
    }
}
