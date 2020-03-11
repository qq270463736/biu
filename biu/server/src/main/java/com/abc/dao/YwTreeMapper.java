package com.abc.dao;

import com.abc.entity.SysPerm;
import com.abc.entity.YwTree;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YwTreeMapper extends BaseMapper<YwTree> {

    void saveOrUpdate(@Param("perms") List<YwTree> perms);
}
