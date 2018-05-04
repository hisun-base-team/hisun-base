/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.controller;

import com.hisun.base.controller.databinder.StringEscapeEditor;
import org.apache.log4j.Logger;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * @author Rocky {rockwithyou@126.com}
 */
public abstract class BaseController {

	//一、关于@PathVariable、@ResponseBody、@RequestParam、@SessionAttributes、@ModelAttribute的说明------------\\
	//1、处理HttpServletRequest的URI部分的注解：@PathVariable----------------------------------------------\\
	//@PathVariable("monitorId")，相当于取得/monitor/delete/{monitorId}的uri中的{monitorId}的值。
	//--------------------------------------------------------------------------------------------------\\
	
	//2、处理HttpServletRequest的body部分的注解：@ResponseBody,@RequestParam---------------------------------\\
	//@RequestParam：相当于request.getParameter(..),包括URL中&?之后的参数，以及POST过来的reqeust中body中的值。
	//@RequestParam有两个属性value、required。
	//value：value为request中属性名称，列如request中有属性monitorName
	//当在在Controller方法的入口参数name，通过(@RequestParam(value = "monitorName") String name)
	//可以将monitorName中的值绑定到name。
	//required：默认为true，用来标识参数是否必须绑定。
	//@ResponseBody：使用系统默认配置的HttpMessageConverter进行解析。 
	//以GET、POST方式提交时，根据request header Content-Type的值来判断:
	//a.multipart/form-data, 使用@RequestBody不能处理这种格式的数据;
	//b.application/x-www-form-urlencoded,可使用@RequestBody;
	//c.其他格式包括:application/json, application/xml等,必须使用@RequestBody来处理。
	//使用@ResponseBody返回的不是常用视图html或JSP，而是其他某种格式数据，通常为JSON。
	//----------------------------------------------------------------------------------------------------\\
	//3、处理HttpSession的Attribute部分的注解：@SessionAttributes----------------------------------------------\\
	//@SessionAttributes：相当于session.setAttribute(..),允许将部分属性转存到session中。
	//@ModelAttribute：用于参数之上时，其首先从session中获取对象的值，如果没有则取request中的body的值填充对应的对象，如果没有
	//则实例化一个。
	//-----------------------------------------------------------------------------------------------------\\
	//注：
	//绑定的简单类型参数：使用@RequestParam，列如：int,Integer,String等等  
	//绑定的复杂类型参数：使用@ModelAttribute，列如：定义的JavaBean，如：Monitor,MonitorVo等等。
	//如果对参数不指定注解，将按照以上方式进行默认绑定。
	
	//二、关于URI定义规范、方法命名定义规范-------------------------------------------------------------------------\\
	//Conroller类上的@RequestMapping，一般以包名或子系统名称作为第一层，第二层为当前实体名称。如：("/examplesys/user")
	//列表页面方法：list(..)     =====> uri资源路径：“/list”              =====>访问方式：GET;
	//增加页面方法：add(..)      =====> uri资源路径：“/add”               =====>访问方式：GET;
	//修改页面方法：edit(..)     =====> uri资源路径：“/edit/{entityId}”   =====>访问方式：GET;
	//保存方法：   save(..)     =====> uri资源路径：“/save”              =====>访问方式：POST;
	//修改方法：   update(..)   =====> uri资源路径：“/update”            =====>访问方式：POST;
	//删除方法：   delete(..)   =====> uri资源路径：“/delete/{entityId}” =====>访问方式：GET;
	//读取方法：   get(..)      =====> uri资源路径：“/{entityId}”        =====>访问方式：GET;
	//读取所有方法: getAll(..)   =====> uri资源路径：“”                   =====>访问方式：GET;
	
	//三、关于方法体返回值的说明----------------------------------------------------------------------------------\\
	//Controller方法返回值分为有无@ResponseBody标签情况，以下为无@ResponseBody情况下的说明
	//1、ModelAndView:返回的是一个包含模型及视图的对象。ModelAndView.setViewName(..)指定需要返回的视图，
	//addObject(name,value)返回模型，等价于request.setAttribute(..)
	//2、ModelMap/Model：返回模型，等价于request.setAttribute(..)。未指定视图，默认以当前请求视图或路径作为视图。
	//3、View：返回一个视图对象。
	//4、String：返回视图名称。
	//5、Void：未指定视图及模型，默认以当前请求视图或路径作为视图名。
	//有@ResponseBody，返回值将放置在BODY区。
	protected final Logger logger = Logger.getLogger(getClass());
	protected final int DEFAULT_PAGE_SIZE = 10;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringEscapeEditor(false, false, false));
	}

}
