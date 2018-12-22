package com.maomaoyu.zhihu.bean;

import org.springframework.stereotype.Component;

/**
 * maomaoyu    2018/12/19_14:47
 **/
@Component
public class HostHandler {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
