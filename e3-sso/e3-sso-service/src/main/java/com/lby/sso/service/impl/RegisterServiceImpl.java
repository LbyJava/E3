package com.lby.sso.service.impl;

import com.lby.common.utils.E3Result;
import com.lby.mapper.TbUserMapper;
import com.lby.pojo.TbUser;
import com.lby.pojo.TbUserExample;
import com.lby.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: TSF
 * @Description:用户注册处理
 * @Date: Create in 2018/12/23 15:53
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public E3Result checkData(String param, int type) {
        //根据不同的type生成不同的查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //1：用户名2：手机号3：邮箱
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        } else if (type == 3) {
            criteria.andEmailEqualTo(param);
        } else {
            return E3Result.build(400, "数据类型错误");
        }
        //执行查询
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        //判断结果中是否包含数据
        if (tbUsers != null && tbUsers.size() > 0) {
            //如果有数据返回false
            return E3Result.ok(false);
        }
        // 如果没有数据返回true
        return E3Result.ok(true);
    }

    @Override
    public E3Result register(TbUser tbUser) {
        //对数据有效性校验
        if (StringUtils.isBlank(tbUser.getUsername()) || StringUtils.isBlank(tbUser.getPassword()) || StringUtils.isBlank(tbUser.getPhone())) {
            return E3Result.build(400, "用户数据不完整，注册失败");
        }
        E3Result e3Result = checkData(tbUser.getUsername(), 1);
        if (!(Boolean) e3Result.getData()) {
            return E3Result.build(400, "此用户名被占用，注册失败");
        }
        E3Result e3Result1 = checkData(tbUser.getPhone(), 2);
        if (!(Boolean) e3Result1.getData()) {
            return E3Result.build(400, "此手机号被占用，注册失败");
        }
        //补全属性
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        //对password经行md5加密
        String md5Pass = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(md5Pass);
        // 把用户数据插入到数据库
        tbUserMapper.insert(tbUser);
        //返回添加成功
        return E3Result.ok();
    }
}
