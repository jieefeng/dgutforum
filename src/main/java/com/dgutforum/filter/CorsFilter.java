package com.dgutforum.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.LogRecord;

@WebFilter(filterName = "CorsFilter", urlPatterns = "/*")  // 拦截所有请求
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // 设置 CORS 相关的响应头
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");  // 允许的跨域源
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"); // 允许的请求方法
        httpResponse.setHeader("Access-Control-Allow-Headers", "token, Content-Type, X-Requested-With"); // 允许的自定义请求头
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); // 允许发送凭证（cookies, token等）
        httpResponse.setHeader("Access-Control-Max-Age", "3600"); // 预检请求的缓存时间，单位为秒

        // 处理预检请求 (OPTIONS 请求)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);  // 返回 200 状态，表示允许跨域
            return;
        }

        // 继续执行过滤链
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
