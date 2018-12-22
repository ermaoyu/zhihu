package com.maomaoyu.zhihu.service;

import com.maomaoyu.zhihu.bean.Question;
import com.maomaoyu.zhihu.dao.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * maomaoyu    2018/12/16_15:50
 **/
@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    SensitiveService sensitiveService;


    public List<Question> getLatestQuestions(int userId,int offset,int limit){
        return questionMapper.selectLatestQuestion(userId,offset,limit);
    }

    public int addQuestion(Question question){
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        //敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionMapper.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public int updateCommentCount(int id,int count){
        return 0;
    }
}
