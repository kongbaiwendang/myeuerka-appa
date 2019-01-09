package com.wyd.bootstrap.security.mapper;

import com.wyd.bootstrap.security.entity.model.user.UserAuxiliary;
import com.wyd.bootstrap.security.entity.model.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAuxiliaryMapper {
    int deleteByPrimaryKey(String userInfoId);

    int insert(UserAuxiliary record);

    int insertSelective(UserAuxiliary record);

    UserAuxiliary selectByPrimaryKey(String userInfoId);

    int updateByPrimaryKeySelective(UserAuxiliary record);

    int updateByPrimaryKey(UserAuxiliary record);

    UserInfo getUserInfoByOpenId(@Param("openId") String openId);

    UserAuxiliary selectByOpenId(@Param("openId") String openId);

    int insertOrUpdateUserAuxiliary(UserAuxiliary record);
}