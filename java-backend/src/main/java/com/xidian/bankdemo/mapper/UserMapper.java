package com.xidian.bankdemo.mapper;

import com.xidian.bankdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User findById(@Param("id") Long id);// FIXME delete this
    User findByName(@Param("name") String name);// FIXME delete this
    List<User> findAll();
    String findPwdByName(@Param("username") String username);
    boolean existsByName(@Param("username") String username);
    int insertUser(User user);
    int updateUser(User user);
    int deleteUser(@Param("id") Long id);
    User getInfoByName(@Param("name") String name);
    int setInfoByName(User info);
    int setPwdByName(@Param("username") String username, @Param("password") String password);
    int setPpwdByNameAndIdCard(@Param("username") String username, @Param("idCard") String idCard,@Param("payPassword") String payPassword);
    int updateCode(@Param("id") Long id, @Param("code") int code);
    boolean existsByNameAndCode(@Param("username") String username, @Param("code") int code);
}
