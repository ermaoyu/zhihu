package com.maomaoyu.zhihu.service;

import org.springframework.stereotype.Service;

/**
 * maomaoyu    2018/12/14_17:49
 **/
@Service
public class WendaService {

    public String getMessage(int userId){
        return "Hello Message: " + String.valueOf(userId);
    }
}

