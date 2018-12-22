package com.maomaoyu.zhihu.dao;

import com.maomaoyu.zhihu.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * maomaoyu    2018/12/16_13:06
 **/
@Mapper
public interface UserMapper {
    String TABLE_NAME = " user ";
    String INSERT_FIELD = " name , password , salt , head_url , email , status";
    String SELECT_FIELD = "id," + INSERT_FIELD;

    @Insert({"insert into ",TABLE_NAME," (",INSERT_FIELD,") values(#{name},#{password},#{salt},#{head_url},#{email},#{status})"})
    int addUser(User user);

    @Select({"select ",SELECT_FIELD," from ",TABLE_NAME," where id=#{id} and status=0" })
    User findById(int id);

    @Select({"select ",SELECT_FIELD," from ",TABLE_NAME," where name=#{name} and status=0" })
    User findByName(String name);

    @Select({"select ",SELECT_FIELD," from ",TABLE_NAME," where email=#{email} and status=0" })
    User findByEmail(String email);

    @Select({"select ",SELECT_FIELD," from ",TABLE_NAME," where status=0"})
    List<User> findAll();

    @Update({"update ",TABLE_NAME," set name=#{name},password=#{password},salt=#{salt},head_url=#{head_url},email=#{email} where id=#{id} and status=0"})
    int updateById(User user);

    @Update({"update ",TABLE_NAME," set status=1 where id=#{id}"})
    int deleteById(int id);

    @Update({"update ",TABLE_NAME," set password=#{password} where id=#{id}"})
    int updatePassword(User user);
}
