package com.maomaoyu.zhihu.bean;

/**
 * maomaoyu    2018/12/14_16:41
 **/
public class User {
    private  String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription(){
        return "This is " + name;
    }
}
