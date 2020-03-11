package com.abc.service;

import com.abc.entity.SysPerm;
import com.abc.entity.YwTree;
import com.abc.vo.AuthVo;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Set;

public interface YwTreeService extends IService<YwTree> {

    void saveOrUpdate(List<YwTree> perms);

}
