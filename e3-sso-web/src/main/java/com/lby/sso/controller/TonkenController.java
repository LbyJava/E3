package com.lby.sso.controller;

import com.lby.common.utils.E3Result;
import com.lby.common.utils.JsonUtils;
import com.lby.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/24 0:17
 */
@Controller
public class TonkenController {
    @Autowired
    private TokenService tokenService;

    /**
     *
     * @return
     * @throws Exception
     */
//    @RequestMapping(value = "/user/token/{token}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE/*"application/json;charset=utf-8"*/)
//    @ResponseBody
//    public String getUserByToken(@PathVariable String token,String callback) throws Exception{
//        E3Result userByToken = tokenService.getUserByToken(token);
//        //响应结果之前，判断是否为jsonp请求
//        if (StringUtils.isNotBlank(callback)) {
//            //把结果封装成一个js语句响应
//            return callback + "(" + JsonUtils.objectToJson(userByToken) + ");";
//        }
//        return JsonUtils.objectToJson(userByToken);
//    }

    /**
     * Spring4.1版本后
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token,String callback) throws Exception{
        E3Result userByToken = tokenService.getUserByToken(token);
        //响应结果之前，判断是否为jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            //把结果封装成一个js语句响应
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userByToken);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return userByToken;
    }
}
