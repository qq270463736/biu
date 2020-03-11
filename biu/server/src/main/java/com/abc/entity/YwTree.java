package com.abc.entity;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@TableName("yw_tree")
public class YwTree extends Model<SysPerm> {

  private String pval;
  private String parent;
  private String pname;
  private Integer ptype;
  private Boolean leaf;
  private String ylid;
  private Date created;
  private Date updated;

  @TableField(exist = false)
  private List<SysPerm> children = new ArrayList<>();

  @Override
  protected Serializable pkVal() {
    return pval;
  }

  public String getPval() {
    return pval;
  }

  public void setPval(String pval) {
    this.pval = pval;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public String getPname() {
    return pname;
  }

  public void setPname(String pname) {
    this.pname = pname;
  }

  public Integer getPtype() {
    return ptype;
  }

  public void setPtype(Integer ptype) {
    this.ptype = ptype;
  }

  public Boolean getLeaf() {
    return leaf;
  }

  public void setLeaf(Boolean leaf) {
    this.leaf = leaf;
  }

  public String getYlid() {
    return ylid;
  }

  public void setYlid(String ylid) {
    this.ylid = ylid;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }
}
