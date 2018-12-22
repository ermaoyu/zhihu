package com.maomaoyu.zhihu.service;

import com.maomaoyu.zhihu.bean.LoginTicket;
import com.maomaoyu.zhihu.bean.User;
import com.maomaoyu.zhihu.dao.LoginTicketMapper;
import com.maomaoyu.zhihu.dao.UserMapper;
import com.maomaoyu.zhihu.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * maomaoyu    2018/12/16_15:50
 **/
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper ticketMapper;

    public User getUser(int id){
        return userMapper.findById(id);
    }

    public Map<String ,Object> register(String username,String password,String email){
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        if (StringUtils.isBlank(email)){
            map.put("msg","邮箱不能为空");
            return map;
        }

        User user = userMapper.findByName(username);
        if (user != null){
            map.put("msg","用户名已经被注册");
            return map;
        }

//        User checkEmail = userMapper.findByEmail(username);
//        if (checkEmail != null){
//            map.put("msg","该邮箱已经被注册");
//            return map;
//        }

        //给密码加MD5盐
        user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHead_url(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userMapper.addUser(user);

        //登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String,Object> login(String username, String password){
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userMapper.findByName(username);
        if (user == null){
            map.put("msg","用户名错误");
            return map;
        }
        if (!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    private String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        ticketMapper.addTicket(ticket);
        return ticket.getTicket();
    }

    public void logout(String ticket){
        ticketMapper.updateTicketStatus(ticket,1);
    }

}
