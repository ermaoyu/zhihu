package com.maomaoyu.zhihu.dao;

import com.maomaoyu.zhihu.bean.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * maomaoyu    2018/12/16_14:13
 **/
@Mapper
public interface QuestionMapper {
    String TABLE_NAME = "question";
    String INSERT_FIELD = " title, content, user_id, created_date, comment_count, status";
    String SELECT_FIELD = "id, " + INSERT_FIELD;

    @Select({"insert into ",TABLE_NAME," (",INSERT_FIELD,") values(#{title},#{content},#{userId},#{createdDate},#{commentCount},#{status})"})
    void addQuestion(Question question);

    List<Question> selectLatestQuestion(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);
}
