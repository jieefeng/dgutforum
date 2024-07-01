package com.dgutforum.filter;


import com.alibaba.fastjson.JSONObject;
import com.dgutforum.common.result.Result;
import com.dgutforum.common.util.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class loginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURL().toString();
        log.info("请求的url：{}",url);

        if(url.contains("login")){
            log.info("登录，放行");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = request.getHeader("token");

        if(!StringUtils.hasLength(jwt)){
            log.info("token为空");

            Result error = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return;
        }

        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("令牌解析失败");
            Result error = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return;
        }

        log.info("解析成功");
        filterChain.doFilter(request, response);
    }

}
