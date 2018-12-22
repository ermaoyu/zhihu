package com.maomaoyu.zhihu.interceptor;

import com.maomaoyu.zhihu.bean.HostHandler;
import com.maomaoyu.zhihu.bean.LoginTicket;
import com.maomaoyu.zhihu.bean.User;
import com.maomaoyu.zhihu.dao.LoginTicketMapper;
import com.maomaoyu.zhihu.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * maomaoyu    2018/12/21_18:20
 **/
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketMapper ticketMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HostHandler hostHandler;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies() != null){
            for (Cookie cookie : httpServletRequest.getCookies()){
                if (cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        if (ticket != null ){
            LoginTicket loginTicket = ticketMapper.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() == 1){
                return true;
            }
            User user = userMapper.findById(loginTicket.getUserId());
            hostHandler.setUser(user);
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null && hostHandler.getUser() != null){
            modelAndView.addObject("user",hostHandler.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHandler.clear();
    }
}
