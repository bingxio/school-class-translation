package com.meniao.classweb.db;

import cn.bmob.v3.BmobUser;

/**
 * Created by Meniao Company on 2017/9/10.
 */

public class User extends BmobUser {
    private String phone;
    private String qq;
    private String weixin;
    private Integer integer;
    private Integer notice;

    public Integer getNotice() {
        return notice;
    }

    public void setNotice(Integer notice) {
        this.notice = notice;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getPhone() {
        return phone;
    }

    public String getQq() {
        return qq;
    }

    public String getWeixin() {
        return weixin;
    }
}
