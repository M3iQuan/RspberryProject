package com.yinxiang.raspberry.validate.code;






import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import org.springframework.util.StringUtils;

import org.springframework.web.filter.GenericFilterBean;



import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ValidateCodeFilter extends GenericFilterBean {
    private String defaultFilterProcessUrl = "/login";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain)
                throws IOException,ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if("POST".equalsIgnoreCase(request.getMethod())&&defaultFilterProcessUrl.equals(request.getServletPath())) {
            String requestCaptcha = request.getParameter("imageCode"); //!!!
            System.out.println("request:"+request.toString());
            //String genCaptcha =requestCaptcha;
            String genCaptcha = (String) request.getSession().getAttribute("imageCode");
            PrintWriter out = res.getWriter();  //不可以用getwriter（）方法来返回json数据了，因为ValidateController有用到getOutputStream,这两个不可以同时使用
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json;charset=utf-8");
            Map<String, Object> map = new HashMap<>();
            System.out.println("genCaptcha:"+genCaptcha);
            System.out.println("requestCaptcha:"+requestCaptcha);
            if(StringUtils.isEmpty(requestCaptcha)) {

                //((HttpServletResponse) res).sendError(400);
                map.put("status",400);
                ObjectMapper om = new ObjectMapper();
                out.write(om.writeValueAsString(map));
                out.flush();
                out.close();
                throw new AuthenticationServiceException("验证码不能为空!");
            }
            if(!genCaptcha.toLowerCase().equals(requestCaptcha.toLowerCase())) {

                //((HttpServletResponse) res).setStatus(400);
                map.put("status",400);
                ObjectMapper om = new ObjectMapper();
                out.write(om.writeValueAsString(map));
                out.flush();
                out.close();

                throw new AuthenticationServiceException("验证码不正确!");
            }


        }
        chain.doFilter(request,response);
    }
}
