package com.lpc.androidbasedemo.algorithmic.model;

import java.util.List;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/6/28
 * description:
 */
public abstract class IAlgorithmicInfo {
    private String name;
    private String result;
    public int id;

    public String getResult() {
        return result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName() {
        return name;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract List<IAlgorithmicInfo> getAllAlgorithmicInfos();
}
