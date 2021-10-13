package com.avic.demo.sebeiglzx.sort;

import com.avic.demo.sebeiglzx.bean.PddMxBean;
import com.avic.demo.sebeiglzx.bean.ProRfidFkbBean;

import java.util.Comparator;

public class MesRfidFkbSort implements Comparator<PddMxBean> {
    @Override
    public int compare(PddMxBean arg0, PddMxBean arg1) {
        /**
         * 根据ischeck排序
         */
        return arg0.getIsCheck().compareTo(arg1.getIsCheck());
    }

}
