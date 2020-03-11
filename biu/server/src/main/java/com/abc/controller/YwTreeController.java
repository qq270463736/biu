package com.abc.controller;

import com.abc.annotation.PermInfo;
import com.abc.constant.PermType;
import com.abc.entity.YwTree;
import com.abc.service.YwTreeService;
import com.abc.vo.Json;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * created by CaiBaoHong at 2018/4/17 16:41<br>
 */
@RestController
@RequestMapping("/yw_tree")
public class YwTreeController {

    private static final Logger log = LoggerFactory.getLogger(YwTreeController.class);

    @Autowired
    private YwTreeService ywTreeService;

    @GetMapping("/list/all")
    public Json listAllPermission() {
        String oper = "list menu,button,api permissions";
        EntityWrapper<YwTree> params = new EntityWrapper<>();
        params.in("ptype", new Integer[]{PermType.MENU, PermType.BUTTON, PermType.API});
        List<YwTree> list = ywTreeService.selectList(params);
        if (list.isEmpty()){
            return Json.succ(oper);
        }else{
            Map<Integer, List<YwTree>> permMap = list.stream().collect(Collectors.groupingBy(YwTree::getPtype));
            List<YwTree> buttonPermList = permMap.get(PermType.BUTTON);
            Map<String, List<YwTree>> buttonsGroupedByParent = new HashMap<>();
            if (buttonPermList!=null&&!buttonPermList.isEmpty()){
                buttonsGroupedByParent = buttonPermList.stream().collect(Collectors.groupingBy(YwTree::getParent));
            }
            return Json.succ(oper, "permMap", permMap).data("btnPermMap", buttonsGroupedByParent);
        }
    }

    @GetMapping("/list/btn_perm_map")
    public Json listButtonPermMapGroupByParent() {
        String oper = "list btn perm map group by parent";
        EntityWrapper<YwTree> params = new EntityWrapper<>();
        params.eq("ptype", PermType.BUTTON);
        List<YwTree> buttonPermList = ywTreeService.selectList(params);

        Map<String, List<YwTree>> buttonsGroupedByParent = new HashMap<>();
        if (buttonPermList!=null&&!buttonPermList.isEmpty()){
            buttonsGroupedByParent = buttonPermList.stream().collect(Collectors.groupingBy(YwTree::getParent));
        }
        return Json.succ(oper, "btnPermMap", buttonsGroupedByParent);
    }

    @PostMapping("/sync/menu")
    public Json syncMenuPermission(@RequestBody String body) {
        String oper = "sync menu permission";
        log.info("{}, body: {}", oper, body);
        List<YwTree> notSyncedPerms = JSON.parseArray(body, YwTree.class);
        if (!notSyncedPerms.isEmpty()){
            ywTreeService.delete(new EntityWrapper<YwTree>().eq("ptype",PermType.MENU));
            ywTreeService.saveOrUpdate(notSyncedPerms);
        }
        return Json.succ(oper);
    }

    @PostMapping("/sync/api")
    public Json syncApiPermission(@RequestBody String body) {
        String oper = "sync api permission";
        log.info("{}, body: {}", oper, body);
        List<YwTree> notSyncedPerms = JSON.parseArray(body, YwTree.class);
        if (!notSyncedPerms.isEmpty()){
            ywTreeService.delete(new EntityWrapper<YwTree>().eq("ptype",PermType.API));
            ywTreeService.saveOrUpdate(notSyncedPerms);
        }
        return Json.succ(oper);
    }

    @PostMapping
    public Json add(@RequestBody String body) {

        String oper = "add permission";
        YwTree perm = JSON.parseObject(body, YwTree.class);

        if (StringUtils.isEmpty(perm.getPval())) {
            return Json.fail(oper, "权限值不能为空");
        }

        EntityWrapper<YwTree> params = new EntityWrapper<>();
        params.eq("pval", perm.getPval());
        params.setSqlSelect("pname,pval");
        YwTree permDB = ywTreeService.selectOne(params);

        if (permDB != null) {
            return Json.fail(oper, "权限值已存在：" + permDB.getPname() + "（" + perm.getPval() + "）");
        }

        //保存
        perm.setCreated(new Date());
        boolean success = ywTreeService.insert(perm);
        return Json.result(oper, success)
                .data("created", perm.getCreated());
    }

    @DeleteMapping
    public Json delete(@RequestBody String body) {
        String oper = "delete permission";
        log.info("{}, body: {}", oper, body);
        JSONObject jsonObj = JSON.parseObject(body);
        String pval = jsonObj.getString("pval");
        if (StringUtils.isBlank(pval)) {
            return Json.fail(oper, "无法删除权限：参数为空（权限值）");
        }
        boolean success = ywTreeService.deleteById(pval);
        return Json.result(oper, success);
    }

    @PatchMapping("/info")
    public Json update(@RequestBody String body) {

        String oper = "update permission";
        log.info("{}, body: {}", oper, body);

        YwTree perm = JSON.parseObject(body, YwTree.class);
        if (StringUtils.isBlank(perm.getPval())) {
            return Json.fail(oper, "无法更新权限：参数为空（权限值）");
        }

        YwTree updateData = new YwTree();
        updateData.setPval(perm.getPval());
        updateData.setPname(perm.getPname());
        updateData.setUpdated(new Date());
        boolean success = ywTreeService.updateById(updateData);
        return Json.result(oper, success).data("updated", perm.getUpdated());
    }
}
