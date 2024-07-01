package com.dgutforum.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(urlPatterns = "/*",filterName = "GlobalFilter")
public class GlobalFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 将ServletRequest转换为HttpServletRequest以访问更多方法
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        // 获取请求的URI
        String requestURI = httpRequest.getRequestURI();

        // 检查是否为登录请求
        if (requestURI.equals("/login")) {
            System.out.println("Login request received at: " + System.currentTimeMillis());
        }
    }
}















