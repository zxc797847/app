package com.avic.demo.sebeiglzx.utils.bean_implement;

import com.avic.demo.sebeiglzx.bean.SbztBean;

import java.util.ArrayList;
import java.util.List;

public class SbztImplement {
    private  static List<SbztBean> sbztList = new ArrayList<SbztBean>();
    static String Text="";
    public static String sbztText(String name){
        String str=name;
        sbztList.add(new SbztBean("0","完好","wanh"));
        sbztList.add(new SbztBean("1","闲置","xianzhi"));
        sbztList.add(new SbztBean("2","报废","baofei"));
        sbztList.add(new SbztBean("3","已借出","yijiechu"));
        sbztList.add(new SbztBean("4","已归还","yiguihuan"));
        sbztList.add(new SbztBean("5","",""));
        for(int i=0;i<sbztList.size();i++){
            if(str.equals(sbztList.get(i).getName())){
                Text=sbztList.get(i).getValue();
                break;
            }else if(str.equals(sbztList.get(i).getValue())){
                Text=sbztList.get(i).getName();
                break;
            }
        }
        return Text;
    }

}
