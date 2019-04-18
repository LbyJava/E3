package com.lby.sso.service;

import com.lby.common.utils.E3Result;
import com.lby.pojo.TbUser;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/23 15:51
 */
public interface RegisterService {
    /**
     * 检验数据有效性
     * @param param 需要校验的数据
     * @param type 校验的数据类型 1-用户名 2-手机号 3-邮箱
     * @return
     */
    E3Result checkData(String param,int type);

    /**
     * 添加用户
     * @param tbUser 用户信息
     * @return
     */
    E3Result register(TbUser tbUser);

}
