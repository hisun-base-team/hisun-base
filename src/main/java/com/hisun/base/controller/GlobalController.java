/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.controller;

import org.quartz.CronExpression;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rocky {rockwithyou@126.com}
 */
@Controller
@RequestMapping("")
public class GlobalController {

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String accessDenied(HttpServletRequest req, Model model) {
        return "error/accessDenied";
    }

    @RequestMapping(value="/validate/cron")
    public @ResponseBody Map<String,Object> validateCron(String cron){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("validate", CronExpression.isValidExpression(cron));
        return map;
    }
}
