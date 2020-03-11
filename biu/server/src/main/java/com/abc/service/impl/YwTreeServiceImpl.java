package com.abc.service.impl;

import com.abc.dao.YwTreeMapper;
import com.abc.entity.SysPerm;
import com.abc.entity.YwTree;
import com.abc.service.SysPermService;
import com.abc.service.YwTreeService;
import com.abc.vo.AuthVo;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class YwTreeServiceImpl extends ServiceImpl<YwTreeMapper, YwTree> implements YwTreeService {

    @Override
    public void saveOrUpdate(List<YwTree> perms) {
        if (perms!=null&&!perms.isEmpty()){
            baseMapper.saveOrUpdate(perms);
        }
    }

}
