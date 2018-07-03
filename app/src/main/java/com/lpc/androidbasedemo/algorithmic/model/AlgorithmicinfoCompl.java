package com.lpc.androidbasedemo.algorithmic.model;

import com.lpc.androidbasedemo.algorithmic.utils.Costants;

import java.util.ArrayList;
import java.util.List;

/*
 * @author lipengcheng
 * @emil lipengcheng8616@163.com
 * create at  2018/6/28
 * description:
 */
public class AlgorithmicinfoCompl extends IAlgorithmicInfo {
    @Override
    public List<IAlgorithmicInfo> getAllAlgorithmicInfos() {
        List<IAlgorithmicInfo> list = new ArrayList<>();
        IAlgorithmicInfo fastAlgorithmicInfo = new AlgorithmicinfoCompl();
        fastAlgorithmicInfo.setId(Costants.ALGORITHMIC_ID_FASTSORT);
        fastAlgorithmicInfo.setName("快速排序");
        list.add(fastAlgorithmicInfo);
        list.add(fastAlgorithmicInfo);
        list.add(fastAlgorithmicInfo);
        list.add(fastAlgorithmicInfo);
        list.add(fastAlgorithmicInfo);
        return list;
    }
}
