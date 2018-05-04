/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.exception;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Rocky {rockwithyou@126.com}
 */
public class GenericSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver{

	 @Override  
	 protected ModelAndView doResolveException(HttpServletRequest request,  
	            HttpServletResponse response, Object handler, Exception ex) {  
	        String viewName = determineViewName(ex, request);
	        logException(ex, request);
	        if (viewName != null) {
	        	//返回为JSP视图
	            if (!(request.getHeader("accept").indexOf("application/json") > -1
						|| (request.getHeader("X-Requested-With")!= null
						    && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
	                // Apply HTTP status code for error views, if specified.  
	                // Only apply it if we're processing a top-level request.  
	                Integer statusCode = determineStatusCode(request, viewName);  
	                if (statusCode != null) {  
	                    applyStatusCodeIfPossible(request, response, statusCode);  
	                }  
	                return getModelAndView(viewName, ex, request);  
	            } else {
	            	//JSON格式
	                try {  
	                	response.setContentType("application/json");
	                    PrintWriter writer = response.getWriter();  
	                    writer.write("{\"success\":\"false\",\"code\":-1,\"message\":\"系统错误\"}");
	                    writer.flush();  
	                } catch (IOException e) {  
						logger.error(e,e);
	                }  
	                return null;  
	  
	            }  
	        } else {  
	            return null;  
	        }  
	    } 
}
