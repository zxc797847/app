package com.avic.demo.sebeiglzx.utils.bean_implement;

import com.avic.demo.sebeiglzx.bean.SbjldwBean;

import org.w3c.dom.Text;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class SbjldwImplement {

    private static List<SbjldwBean> sbjldwList = new ArrayList<SbjldwBean>();
    static String Text="";

    public static String jildwText(String name){
        String str=name;
        sbjldwList.add(new SbjldwBean("0","只","zhi"));
        sbjldwList.add(new SbjldwBean("1","箱","xiang"));
        sbjldwList.add(new SbjldwBean("2","个","ge"));
        sbjldwList.add(new SbjldwBean("3","片","pian"));
        sbjldwList.add(new SbjldwBean("4","公斤","gongjin"));
        sbjldwList.add(new SbjldwBean("5","米","mi"));
        sbjldwList.add(new SbjldwBean("6","根","gen"));
        sbjldwList.add(new SbjldwBean("7","套","tao"));
        sbjldwList.add(new SbjldwBean("8","盒","he"));
        sbjldwList.add(new SbjldwBean("9","条","tiao"));
        sbjldwList.add(new SbjldwBean("10","台","tai"));
        sbjldwList.add(new SbjldwBean("11","付","fu"));
        sbjldwList.add(new SbjldwBean("12","件","jian"));
        sbjldwList.add(new SbjldwBean("13","块","kuai"));
        sbjldwList.add(new SbjldwBean("14","支","zhis"));
        sbjldwList.add(new SbjldwBean("15","把","ba"));
        sbjldwList.add(new SbjldwBean("16","双","shuang"));
        sbjldwList.add(new SbjldwBean("17","袋","dai"));
        sbjldwList.add(new SbjldwBean("18","瓶","ping"));
        sbjldwList.add(new SbjldwBean("19","张","zhang"));
        sbjldwList.add(new SbjldwBean("20","卷","juan"));
        sbjldwList.add(new SbjldwBean("21","桶","tong"));
        sbjldwList.add(new SbjldwBean("22","圈","quan"));
        sbjldwList.add(new SbjldwBean("23","颗","ke"));
        sbjldwList.add(new SbjldwBean("24","盏","zhan"));
        sbjldwList.add(new SbjldwBean("25","辆","liang"));
        sbjldwList.add(new SbjldwBean("26","齿","chi"));
        sbjldwList.add(new SbjldwBean("27","峰","feng"));
        sbjldwList.add(new SbjldwBean("28","包","bao"));
        sbjldwList.add(new SbjldwBean("29","组","zu"));
        sbjldwList.add(new SbjldwBean("30","本","ben"));
        sbjldwList.add(new SbjldwBean("31","对","dui"));
        sbjldwList.add(new SbjldwBean("32","平方米","m2"));
        sbjldwList.add(new SbjldwBean("33","听","ting"));
        sbjldwList.add(new SbjldwBean("34","罐","guan"));
        sbjldwList.add(new SbjldwBean("35","批","pi"));
        sbjldwList.add(new SbjldwBean("36","毫米","haomi"));
        sbjldwList.add(new SbjldwBean("37","升","sheng"));
        sbjldwList.add(new SbjldwBean("38","副","fus"));
        sbjldwList.add(new SbjldwBean("39","扇","shan"));
        sbjldwList.add(new SbjldwBean("40","节","jie"));
        sbjldwList.add(new SbjldwBean("41","",""));

        for (int i=0;i<sbjldwList.size();i++){
            if (str.equals(sbjldwList.get(i).getValue())){
                Text=sbjldwList.get(i).getName();
                break;
            }else if(str.equals(sbjldwList.get(i).getName())){
                Text=sbjldwList.get(i).getValue();
                break;
            }
        }
        return Text;
    }

    public static Integer getSbjldwNum(String name){
        Integer num=0;
        if ("zhi".equals(name)) {
            num = 0;
        }else if ("xiang".equals(name)) {
            num = 1;
        }else if ("ge".equals(name)) {
            num = 2;
        }else if("pian".equals(name)){
            num=3;
        }else if ("gongjin".equals(name)){
            num=4;
        }else if ("mi".equals(name)){
            num=5;
        }else if ("gen".equals(name)){
            num=6;
        }else if ("tao".equals(name)){
            num=7;
        }else if ("he".equals(name)){
            num=8;
        }else if ("tiao".equals(name)){
            num=9;
        }else if ("tai".equals(name)){
            num=10;
        }else if ("fu".equals(name)){
            num=11;
        }else if ("jian".equals(name)){
            num=12;
        }else if ("kuai".equals(name)){
            num=13;
        }else if ("zhis".equals(name)){
            num=14;
        }else if ("ba".equals(name)){
            num=15;
        }else if ("shuang".equals(name)){
            num=16;
        }else if ("dai".equals(name)){
            num=17;
        }else if ("ping".equals(name)){
            num=18;
        }else if ("zhang".equals(name)){
            num=19;
        }else if ("juan".equals(name)){
            num=20;
        }else if ("tong".equals(name)){
            num=21;
        }else if ("quan".equals(name)){
            num=22;
        }else if ("ke".equals(name)){
            num=23;
        }else if ("zhan".equals(name)){
            num=24;
        }else if ("liang".equals(name)){
            num=25;
        }else if ("chi".equals(name)){
            num=26;
        }else if ("feng".equals(name)){
            num=27;
        }else if ("bao".equals(name)){
            num=28;
        }else if ("zu".equals(name)){
            num=29;
        }else if ("ben".equals(name)){
            num=30;
        }else if ("dui".equals(name)){
            num=31;
        }else if ("m2".equals(name)){
            num=32;
        }else if ("ting".equals(name)){
            num=33;
        }else if ("guan".equals(name)){
            num=34;
        }else if ("pi".equals(name)){
            num=35;
        }else if ("haomi".equals(name)){
            num=36;
        }else if ("sheng".equals(name)){
            num=37;
        }else if ("fus".equals(name)){
            num=38;
        }else if ("shan".equals(name)){
            num=39;
        }else if ("jie".equals(name)){
            num=40;
        }else if(name.equals("")){
            num=0;
        }
        return num;
    }

}
