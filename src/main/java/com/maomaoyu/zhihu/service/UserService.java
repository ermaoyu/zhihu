package com.maomaoyu.zhihu.service;

import com.maomaoyu.zhihu.bean.User;
import com.maomaoyu.zhihu.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * maomaoyu    2018/12/16_15:50
 **/
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUser(int id){
        return userMapper.findById(id);
    }

}
