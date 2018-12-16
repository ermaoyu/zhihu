package com.maomaoyu.zhihu;

import com.maomaoyu.zhihu.bean.Question;
import com.maomaoyu.zhihu.bean.User;
import com.maomaoyu.zhihu.dao.QuestionMapper;
import com.maomaoyu.zhihu.dao.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

/**
 * maomaoyu    2018/12/16_13:23
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDatabaseTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Test
    public void userContextLoads() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setHead_url(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setEmail("928432997@qq.com");
            user.setSalt("");
            user.setStatus(0);

            userMapper.addUser(user);
        }
    }

    @Test
    public void updateUserTest(){
        for (int i = 1; i <= 10 ; i++) {
            User user = userMapper.findById(i);
            System.out.println(user);
            user.setPassword("123");
            userMapper.updatePassword(user);
        }
        System.out.println(userMapper.findAll());
    }

    @Test
    public void questionContentLoad(){
        for (int i = 0; i < 11 ; i++) {
            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}",i));
            question.setContent(String.format("maomaomaomaomaomaoms Content %d",i));
            questionMapper.addQuestion(question);
        }
    }

    @Test
    public void test(){
        System.out.println(questionMapper.selectLatestQuestion(0,0,10));
    }



}
