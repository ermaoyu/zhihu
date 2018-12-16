package com.maomaoyu.zhihu.service;

import com.maomaoyu.zhihu.bean.Question;
import com.maomaoyu.zhihu.dao.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * maomaoyu    2018/12/16_15:50
 **/
@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;
    public List<Question> getLatestQuestions(int userId,int offset,int limit){
        return questionMapper.selectLatestQuestion(userId,offset,limit);
    }
}
