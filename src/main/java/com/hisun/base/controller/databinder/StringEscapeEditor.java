/*
 * Copyright (c) 2018. Hunan Hisun Union Information Technology Co, Ltd. All rights reserved.
 * http://www.hn-hisun.com
 * 注意:本内容知识产权属于湖南海数互联信息技术有限公司所有,除非取得商业授权,否则不得用于商业目的.
 */

package com.hisun.base.controller.databinder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import java.beans.PropertyEditorSupport;

/**
 * @author Rocky {rockwithyou@126.com}
 */
public class StringEscapeEditor extends PropertyEditorSupport{

    private boolean escapeHTML;

    private boolean escapeJavaScript;

    private boolean escapeSQL;

    public StringEscapeEditor() { super(); }

    public StringEscapeEditor(boolean escapeHTML, boolean escapeJavaScript, boolean escapeSQL) {
        super();
        this.escapeHTML = escapeHTML;
        this.escapeJavaScript = escapeJavaScript;
        this.escapeSQL = escapeSQL;
    }


    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        //super.setAsText(text);
        if (text == null) {
            setValue(null);
        } else {
            String value = text;
            if (escapeHTML) {
                value = HtmlUtils.htmlEscape(value);
            }

            if (escapeJavaScript) {
                value = JavaScriptUtils.javaScriptEscape(value);
            }

            if (escapeSQL) {
                value = this.transactSQLInjection(value);
            }
            setValue(value);
        }
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
    }

    private String transactSQLInjection(String str) {
        if (StringUtils.isNotBlank(str)){
            //单引号(')，分号(;) 和 注释符号(--)
            return str.replaceAll(".*([']+|(--)+).*", " ");
        }else {
            return "";
        }
    }

}
