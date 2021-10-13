package com.avic.demo.sebeiglzx.utils.redcarutil;

import android.app.Application;
import android.util.Log;

import com.avic.demo.sebeiglzx.home.MyApplication;
import com.uhf.api.cls.Reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * bank(过滤)为1为EPC区，2为TID区,3用户区
 */
//读卡
public class ReadRfidCardUtil {
    String val_EPC = "";
    String val_TID = "";
    MyApplication myapp = new MyApplication();
    Reader.TagFilter_ST g2tf = null;
    public MyApplication ReadRfidapp(  Application app){
        myapp = (MyApplication) app;
        myapp.Mreader = new Reader();
        myapp.Rparams = myapp.new ReaderParams();
        g2tf = myapp.Mreader.new TagFilter_ST();
        g2tf = null;
        myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_FILTER, g2tf);
        myapp.Rparams.password = "";
        myapp.initReader();
        return myapp;
    }

    //读卡EPC
    public  String ReadRfidCard_EPC(){
        int ant = 1;
        char bank = 1;
        int addr = 2;
        int blks = 6;
        byte[] data=new byte[12];
        String pwd="0000";
        byte[] pwdb=new byte[4];
        myapp.Mreader.Str2Hex(pwd, pwd.length(), pwdb);
        Reader.READER_ERR err = Reader.READER_ERR.MT_OK_ERR;
        int trycount = 6;
        do {
            err= myapp.Mreader.GetTagData(ant, bank, addr, blks, data, pwdb, (short) 150);
            trycount--;
            if (trycount < 1)
                break;
        } while (err != Reader.READER_ERR.MT_OK_ERR);
        if ( err!=Reader.READER_ERR. MT_OK_ERR) {
            System.out.println("GetTagData failed");
        }else{
            char[] out = null;
            out = new char[data.length * 2];
            myapp.Mreader.Hex2Str(data, data.length, out);
            val_EPC = String.valueOf(out);
        }
        return val_EPC;
    }

    public String ReadRfidCard_TID1(){
        int ant = 1;
        char bank = 2;
        int addr = 0;
        int blks = 6;
        byte[] data=new byte[12];
        String pwd="0000";
        byte[] pwdb=new byte[4];
        myapp.Mreader.Str2Hex(pwd, pwd.length(), pwdb);
        Reader.READER_ERR err = Reader.READER_ERR.MT_OK_ERR;
        int trycount = 6;
        do {
            err= myapp.Mreader.GetTagData(ant, bank, addr, blks, data, pwdb, (short) 150);
            trycount--;
            if (trycount < 1)
                break;
        } while (err != Reader.READER_ERR.MT_OK_ERR);
        if ( err!=Reader.READER_ERR. MT_OK_ERR) {
            System.out.println("GetTagData failed");
        }else{
            char[] out = null;
            out = new char[data.length * 2];
            myapp.Mreader.Hex2Str(data, data.length, out);
            val_TID = String.valueOf(out);
        }
        return val_TID;
    }

    //读卡TID1无缘亮灯
    public String ReadRfidCard_TID(){
        int ant = 1;
        char bank = 2;
        int addr = 0;
        int blks = 4;
        byte[] data=new byte[12];
        String pwd="0000";
        byte[] pwdb=new byte[4];
        myapp.Mreader.Str2Hex(pwd, pwd.length(), pwdb);
        Reader.READER_ERR err = Reader.READER_ERR.MT_OK_ERR;
        int trycount = 6;
        do {
            err= myapp.Mreader.GetTagData(ant, bank, addr, blks, data, pwdb, (short) 150);
            trycount--;
            if (trycount < 1)
                break;
        } while (err != Reader.READER_ERR.MT_OK_ERR);
        if ( err!=Reader.READER_ERR. MT_OK_ERR) {
            System.out.println("GetTagData failed");
        }else{
            char[] out = null;
            out = new char[data.length * 2];
            myapp.Mreader.Hex2Str(data, data.length, out);
            val_TID = String.valueOf(out);
        }
        return val_TID;
    }

    //盘点操作
    public List<String> runnable_epc(){

        int[] tagcnt = new int[1];
        tagcnt[0] = 0;
        String[] tag = null;
        List<String> resultList = new ArrayList<>();
        //mData = null;
        int streadt = 0, enreadt = 0;
        synchronized (this) {
            // Log.d("MYINFO", "ManActivity..1");
            Reader.READER_ERR er;
            streadt = (int) System.currentTimeMillis();
            if (myapp.isquicklymode) {
                er = myapp.Mreader.AsyncGetTagCount(tagcnt);
            } else {
                er = myapp.Mreader.TagInventory_Raw(myapp.Rparams.uants,
                        myapp.Rparams.uants.length,
                        (short) myapp.Rparams.readtime, tagcnt);
            }

            if (er == Reader.READER_ERR.MT_OK_ERR) {
                if (tagcnt[0] > 0) {
//                        tv_once.setText(String.valueOf(tagcnt[0]));

//                        soundPool.play(1, 1, 1, 0, 0, 1);
                    tag = new String[tagcnt[0]];
                    for (int i = 0; i < tagcnt[0]; i++) {
                        Reader.TAGINFO tfs = myapp.Mreader.new TAGINFO();

                        if (myapp.isquicklymode)
                            er = myapp.Mreader.AsyncGetNextTag(tfs);
                        else
                            er = myapp.Mreader.GetNextTag(tfs);
                        if (er == Reader.READER_ERR.MT_HARDWARE_ALERT_ERR_BY_TOO_MANY_RESET) {
                            myapp.needreconnect = true;
                        }
                        if (er == Reader.READER_ERR.MT_OK_ERR) {
                            tag[i] = Reader.bytes_Hexstr(tfs.EpcId);
                            Collections.addAll(resultList,tag);
                            return  resultList;
                        } else
                            break;
                    }
                    enreadt = (int) System.currentTimeMillis();
                }
            }
        }

       return  resultList;
    }

}
