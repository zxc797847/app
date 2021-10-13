package com.avic.demo.sebeiglzx.utils.tools;

import android.widget.RadioGroup;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.activity.WMSRfidInitActivity;
import com.avic.demo.sebeiglzx.bean.MyApplication;
import com.function.commfun;
import com.uhf.api.cls.Reader;

public class RfidRead {

    public static String getTidValue(MyApplication myapp) {
        Reader.TagFilter_ST g2tf = null;
        RadioGroup gr_opant, gr_match, gr_enablefil, gr_wdatatype;
        Reader.READER_ERR er;
        String tid = "";
        try {

            myapp.Rparams.opant = 1;
            myapp.Rparams.password = "";

            if (0 == 1) {
                //EditText et = (EditText) findViewById(R.id.editText_opfilterdata);
                int ln = 16;
                if (ln == 1 || ln % 2 == 1)
                    ln++;
                byte[] fdata = new byte[ln / 2];
                myapp.Mreader.Str2Hex("16", 16, fdata);

                g2tf = myapp.Mreader.new TagFilter_ST();
                g2tf.fdata = fdata;
                g2tf.flen = 4 * 4;
                int ma = commfun.SortGroup(gr_match);
                if (ma == 1)
                    g2tf.isInvert = 1;
                else
                    g2tf.isInvert = 0;

                g2tf.bank = 2 + 1;

                //EditText etadr = (EditText) findViewById(R.id.editText_opfilsadr);
                g2tf.startaddr = Integer.valueOf("6");

                er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_FILTER, g2tf);

            } else {
                g2tf = null;
                myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_FILTER, g2tf);
            }

            byte[] rdata = new byte[6*2];

            byte[] rpaswd = new byte[4];
            if (!myapp.Rparams.password.equals("")) {
                myapp.Mreader.Str2Hex(myapp.Rparams.password,
                        myapp.Rparams.password.length(), rpaswd);
            }

            //Reader.READER_ERR er = Reader.READER_ERR.MT_OK_ERR;
            int trycount = 3;
            do {
                er = myapp.Mreader.GetTagData(myapp.Rparams.opant,(char) 2,0,6,
                        rdata, rpaswd, (short) myapp.Rparams.optime);
                if(er != Reader.READER_ERR.MT_OK_ERR)
                    //myapp.Mreader.GetLastDetailError(myapp.ei);

                    trycount--;
                if (trycount < 1)
                    break;
            } while (er != Reader.READER_ERR.MT_OK_ERR);

            if (er == Reader.READER_ERR.MT_OK_ERR) {
                String val = "";
                char[] out = null;

                out = new char[rdata.length * 2];
                myapp.Mreader.Hex2Str(rdata, rdata.length, out);
                val = String.valueOf(out);
                tid=val;
            } else {

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block

        }
        finally {
            g2tf = null;
            myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_FILTER, g2tf);
        }
        return tid;
    }

    public static String getEpcValue(MyApplication myapp) {
        Reader.TagFilter_ST g2tf = null;
        RadioGroup gr_opant, gr_match, gr_enablefil, gr_wdatatype;
        Reader.READER_ERR er;
        String epc = "";
        try {

            myapp.Rparams.opant = 1;
            myapp.Rparams.password = "";

            if (0 == 1) {
                //EditText et = (EditText) findViewById(R.id.editText_opfilterdata);
                int ln = 16;
                if (ln == 1 || ln % 2 == 1)
                    ln++;
                byte[] fdata = new byte[ln / 2];
                myapp.Mreader.Str2Hex("16", 16, fdata);

                g2tf = myapp.Mreader.new TagFilter_ST();
                g2tf.fdata = fdata;
                g2tf.flen = 4 * 4;
                int ma = commfun.SortGroup(gr_match);
                if (ma == 1)
                    g2tf.isInvert = 1;
                else
                    g2tf.isInvert = 0;

                g2tf.bank = 2 + 1;

                //EditText etadr = (EditText) findViewById(R.id.editText_opfilsadr);
                g2tf.startaddr = Integer.valueOf("6");

                er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_FILTER, g2tf);

            } else {
                g2tf = null;
                myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_FILTER, g2tf);
            }

            byte[] rdata = new byte[6*2];

            byte[] rpaswd = new byte[4];
            if (!myapp.Rparams.password.equals("")) {
                myapp.Mreader.Str2Hex(myapp.Rparams.password,
                        myapp.Rparams.password.length(), rpaswd);
            }

            //Reader.READER_ERR er = Reader.READER_ERR.MT_OK_ERR;
            int trycount = 3;
            do {
                er = myapp.Mreader.GetTagData(myapp.Rparams.opant,(char) 1,2,6,
                        rdata, rpaswd, (short) myapp.Rparams.optime);
                if(er != Reader.READER_ERR.MT_OK_ERR)
                    //myapp.Mreader.GetLastDetailError(myapp.ei);

                    trycount--;
                if (trycount < 1)
                    break;
            } while (er != Reader.READER_ERR.MT_OK_ERR);

            if (er == Reader.READER_ERR.MT_OK_ERR) {
                String val = "";
                char[] out = null;

                out = new char[rdata.length * 2];
                myapp.Mreader.Hex2Str(rdata, rdata.length, out);
                val = String.valueOf(out);
                epc=val;
            } else {

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block

        }
        finally {
            g2tf = null;
            myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_FILTER, g2tf);
        }
        return epc;
    }
}
