package com.maomaoyu.zhihu.interceptor;

import com.maomaoyu.zhihu.bean.HostHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * maomaoyu    2018/12/21_18:17
 **/
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHandler hostHandler;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (hostHandler.getUser() == null){
            httpServletResponse.sendRedirect("/regLogin?next=" + httpServletRequest.getRequestURI());
//            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
