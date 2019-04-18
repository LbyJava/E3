package com.lby.sso.controller;

import com.lby.common.utils.E3Result;
import com.lby.pojo.TbUser;
import com.lby.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: TSF
 * @Description:注册功能controller
 * @Date: Create in 2018/12/22 21:19
 */
@Controller
public class RegitsterController {
    @Autowired
    private RegisterService registerService;

    /**
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/page/register")
    public String showRegister() throws Exception{
    
        return "register";
    }

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkDate(@PathVariable String param, @PathVariable int type) throws Exception {
        E3Result e3Result = registerService.checkData(param, type);
        return e3Result;
    }

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public E3Result register(TbUser tbUser) throws Exception {
        E3Result register = registerService.register(tbUser);
        return register;
    }
}
