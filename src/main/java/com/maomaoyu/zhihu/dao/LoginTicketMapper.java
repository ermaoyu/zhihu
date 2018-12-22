package com.maomaoyu.zhihu.dao;

import com.maomaoyu.zhihu.bean.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * maomaoyu    2018/12/19_14:53
 **/
@Mapper
public interface LoginTicketMapper {
    String TABLE_NAME = "login_ticket";
    String INSERT_FILEDS = "user_id , ticket ,expired , status";
    String SELECT_FILEDS = "id , " + INSERT_FILEDS;

    @Insert({"insert into ",TABLE_NAME," (",INSERT_FILEDS,") values(#{userId},#{ticket},#{expired},#{status})"})
    int addTicket(LoginTicket ticket);

    @Select({"select ",SELECT_FILEDS," from ",TABLE_NAME," where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ",TABLE_NAME," set status=#{status} where ticket=#{ticket}"})
    int updateTicketStatus(@Param("ticket") String ticket,@Param("status") int status);

}
