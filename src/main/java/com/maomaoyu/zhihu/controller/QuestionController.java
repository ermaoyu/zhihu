package com.maomaoyu.zhihu.controller;

import com.maomaoyu.zhihu.bean.HostHandler;
import com.maomaoyu.zhihu.bean.Question;
import com.maomaoyu.zhihu.service.QuestionService;
import com.maomaoyu.zhihu.service.UserService;
import com.maomaoyu.zhihu.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * maomaoyu    2018/12/22_18:49
 **/
@Controller
public class QuestionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHandler hostHandler;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/question/add"} ,method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam String title,@RequestParam String content){
        try {
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            if (hostHandler.getUser() == null){
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
                return WendaUtil.getJSONString(999);
            }else {
                question.setUserId(hostHandler.getUser().getId());
            }
            if (questionService.addQuestion(question) > 0){
                return WendaUtil.getJSONString(0);
            }
        } catch (Exception e) {
            LOGGER.error("增加失败" + e.getMessage());
        }
        return WendaUtil.getJSONString(1,"失败");
    }
}
