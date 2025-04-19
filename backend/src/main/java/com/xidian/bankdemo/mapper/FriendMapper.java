package com.xidian.bankdemo.mapper;

import com.xidian.bankdemo.entity.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FriendMapper {
    List<Friend> findByOwnerId(@Param("ownerId") Long ownerId);
    int insertFriend(Friend friend);
    boolean existsByAccount(@Param("account") Long account, @Param("ownerId") Long ownerId);
}
