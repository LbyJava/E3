package com.lby.sso.service.impl;

import com.lby.common.jedis.JedisClient;
import com.lby.common.utils.E3Result;
import com.lby.common.utils.JsonUtils;
import com.lby.mapper.TbUserMapper;
import com.lby.pojo.TbUser;
import com.lby.pojo.TbUserExample;
import com.lby.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * @Author: TSF
 * @Description:用户登入处理
 * @Date: Create in 2018/12/23 17:46
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;
    @Override
    public E3Result userLogin(String username, String password) {
        //1、判断用户和密码是否正确
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        //执行查询
        List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
        if (list == null || list.size() == 0) {
            //返回登入失败
            return E3Result.build(400, "用户名或密码错误");
        }
        //取用户信息
        TbUser user = list.get(0);
        //判断密码是否正确
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
            //2、如果不正确，返回登录失败
            return E3Result.build(400, "用户名或密码错误");
        }
        //3、如果正确生成token。
        String token = UUID.randomUUID().toString();
        //4、把用户信息写入redis，key:token value：用户信息
        user.setPassword(null);
        jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
        //5、设置Session的过期时间
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        //6、把token返回
        return E3Result.ok(token);
    }
}
