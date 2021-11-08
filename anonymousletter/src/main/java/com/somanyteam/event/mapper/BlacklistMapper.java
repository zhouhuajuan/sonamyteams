package com.somanyteam.event.mapper;

import com.somanyteam.event.entity.Blacklist;
import java.util.List;

public interface BlacklistMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Blacklist record);

    Blacklist selectByPrimaryKey(Long id);

    List<Blacklist> selectAll();

    int updateByPrimaryKey(Blacklist record);
}