package com.avic.demo.sebeiglzx.home;

import android.app.Application;
import android.widget.TabHost;
import android.widget.Toast;

import com.function.SPconfig;
import com.pow.api.cls.RfidPower;
import com.uhf.api.cls.BackReadOption;
import com.uhf.api.cls.Reader;
import com.uhf.api.cls.Reader.TAGINFO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用类，包括全局常量，读写器，以及相关变量参数
 *
 * @author Administrator
 *
 */

public class MyApplication extends Application {

    // 常量( const)
    // *
    public static String Constr_READ = "读";
    public static String Constr_CONNECT = "连接";
    public static String Constr_INVENTORY = "盘点";
    public static String Constr_RWLOP = "读写锁";
    public static String Constr_set = "设置";
    public static String Constr_SetFaill = "设置失败：";
    public static String Constr_GetFaill = "获取失败：";
    public static String Constr_SetOk = "设置成功";
    public static String Constr_unsupport = "不支持";
    public static String Constr_Putandexit = "再按一次退出程序";
    public static String[] Coname = new String[] { "序号", "EPC ID", "次数", "天线",
            "协议", "RSSI", "频率", "TID", "BID", "RFU", "时间戳","附加数据 " };
    public static String Constr_stopscan = "请先停止扫描";
    public static String Constr_hadconnected = "已经连接";
    public static String Constr_plsetuuid = "请设置好UUID:";
    public static String Constr_pwderror = "密码错误";
    public static String Constr_search = "搜索";
    public static String Constr_stop = "停止";

    public static String Constr_createreaderok = "读写器创建失败";
    public static String[] pdaatpot = { "一天线", "双天线", "三天线", "四天线", "16天线" };

    public static String[] spibank = { "保留区", "EPC区", "TID区", "用户区" };
    public static String[] spifbank = { "EPC区", "TID区", "用户区" };
    public static String[] regtype;
    public static String[] spilockbank = { "访问密码", "销毁密码", "EPCbank",
            "TIDbank", "USERbank" };
    public static String[] spilocktype = { "解锁定", "暂时锁定", "永久锁定" };
    public static String Constr_sub3readmem = "读标签";
    public static String Constr_sub3writemem = "写标签";
    public static String Constr_sub3lockkill = "锁与销毁";
    public static String Constr_sub3readfail = "读失败:";
    public static String Constr_sub3nodata = "无数据";
    public static String Constr_sub3wrtieok = "写成功";
    public static String Constr_sub3writefail = "写失败:";
    public static String Constr_sub3lockok = "锁成功";
    public static String Constr_sub3lockfail = "锁失败:";
    public static String Constr_sub3killok = "销毁成功";
    public static String Constr_sub3killfial = "销毁失败:";

    // String[] spireg={"中国","北美","日本","韩国","欧洲","印度","加拿大","全频段"
    // ,"中国2"};
    public static String[] spireg = { "中国", "北美", "日本", "韩国", "欧洲", "欧洲2",
            "欧洲3", "印度", "加拿大", "全频段", "中国2" };
    public static String[] spinvmo = { "普通模式", "高速模式" };
    public static String[] spitari = { "25微秒", "12.5微秒", "6.25微秒" };
    public static String[] spiwmod = { "字写", "块写" };
    public static String Auto = "自动";
    public static String No = "无";
    public static String Constr_sub4invenpra = "盘点参数";
    public static String Constr_sub4antpow = "天线功率";
    public static String Constr_sub4regionfre = "区域频率";
    public static String Constr_sub4gen2opt = "Gen2项";
    public static String Constr_sub4invenfil = "盘点过滤";
    public static String Constr_sub4addidata = "附加数据";
    public static String Constr_sub4others = "其他参数";
    public static String Constr_sub4quickly = "快速模式";
    public static String Constr_sub4setmodefail = "配置模式失败";
    public static String Constr_sub4setokresettoab = "设置成功，重启读写器生效";
    public static String Constr_sub4ndsapow = "该设备需要功率一致";
    public static String Constr_sub4unspreg = "不支持的区域";

    public static String[] spiregbs = { "北美", "中国", "欧频", "中国2" };
    public static String Constr_subblmode = "模式";
    public static String Constr_subblinven = "盘点";
    public static String Constr_subblfil = "过滤";
    public static String Constr_subblfre = "频率";
    public static String Constr_subblnofre = "没有选择频点";

    public static String[] cusreadwrite = { "读操作", "写操作" };
    public static String[] cuslockunlock = { "锁", "解锁" };

    public static String Constr_subcsalterpwd = "改密码";
    public static String Constr_subcslockwpwd = "带密码锁";
    public static String Constr_subcslockwoutpwd = "不带密码锁";
    public static String Constr_subcsplsetimeou = "请设置超时时间";
    public static String Constr_subcsputcnpwd = "填入当前密码与新密码";
    public static String Constr_subcsplselreg = "请选择区域";
    public static String Constr_subcsopfail = "操作失败:";
    public static String Constr_subcsputcurpwd = "填入当前密码";

    public static String Constr_subdbdisconnreconn = "已经断开,正在重新连接";
    public static String Constr_subdbhadconnected = "已经连接";
    public static String Constr_subdbconnecting = "正在连接......";
    public static String Constr_subdbrev = "接收";
    public static String Constr_subdbstop = "停止";
    public static String action_db_down = "";
    public static String Constr_subdbdalennot = "数据长度不对";
    public static String Constr_subdbplpuhexchar = "请输入16进制字符";

    public static String Constr_subsysaveok = "保存成功";
    public static String Constr_subsysout = "输入txt或者csv";
    public static String Constr_subsysreavaid = "重新连接生效";
    public static String Constr_sub1recfailed = "重新连接失败";
    public static String Constr_subsysavefailed = "保存失败";
    public static String Constr_subsysexefin = "执行完毕";
    public static String Constr_sub1adrno = "地址没有输入";
    public static String Constr_sub1pdtsl = "请选择平台";
    public static String Constr_mainpu = "上电：";
    public static String Constr_nostopstreadfailed = "不停顿盘点启动失败";
    public static String Constr_nostopspreadfailed = "不停顿盘点停止失败";
    public static String Constr_nostopreadfailed = "开始盘点失败：";
    public static String Constr_connectok = "连接成功";
    public static String Constr_connectfialed = "连接失败";
    public static String Constr_disconpowdown = "断开读写器，下电：";
    public static String Constr_ok = "成功:";
    public static String Constr_failed = "失败:";
    public static String Constr_excep = "异常:";
    public static String Constr_setcep = "设置异常:";
    public static String Constr_getcep = "获取异常:";
    public static String Constr_killok = "KILL成功";
    public static String Constr_killfailed = "KILL失败";
    public static String Constr_psiant = "请选择盘点天线";
    public static String Constr_selpro = "请选择协议";
    public static String Constr_setpwd = "设置功率:";
    public static String Constr_carry_fftable = "频率-反射 图表";
    public static String Constr_carry_frtable = "频率-回波 图表";
    public static String Constr_carry_binvpw = "盘点前前向功率:		 ";
    public static String Constr_carry_binvfpw = "盘点前反向功率:		 ";
    public static String Constr_carry_invc = "盘点标签个数:		  ";
    public static String Constr_carry_invtep = "盘点温度:		      ";
    public static String Constr_carry_invpw = "盘点前向功率:		   ";
    public static String Constr_carry_ainvfpw = "盘点后反向功率:		 ";
    public static String Constr_carry_ainvpw = "盘点后前向功率 :		";

    // */

    /*
     * 公共变量 public var
     */
    public Map<String, Reader.TAGINFO> TagsMap = new LinkedHashMap<String, Reader.TAGINFO>();// 有序
    // sorted
    public String path;
    public int ThreadMODE = 0;
    public int refreshtime = 1000;
    public int Mode;
    public Map<String, String> m;
    public TabHost tabHost;
    public long exittime;
    public boolean needreconnect;
    public boolean haveinit;
    public com.uhf.api.cls.Reader Mreader;
    public int antportc;
    public String Curepc;
    public int Bank;
    public int BackResult;

    public SPconfig spf;
    public RfidPower Rpower;

    public ReaderParams Rparams;
    public String Address;
    public boolean isquicklymode = false;
    public boolean needlisen = false;
    public int nxpu8 = 0;
    public BackReadOption m_BROption = new BackReadOption();
    public boolean isUniByEmd;// 是否附加数据唯一(whether addition data unique)
    public boolean isUniByAnt;// 是否天线号唯一(whether antenna id unique)
    public long stoptimems;
    public boolean isstoptime;
    public long latetimems;
    public boolean islatetime;
    public Reader.HardwareDetails myhd;
    // 将标签关联物品显示(show the relation good)
    public boolean isconngoods;
    public Map<String, String> listName;
    public int[] allhtb = new int[] { 915750, 915250, 903250, 926750, 926250,
            904250, 927250, 920250, 919250, 909250, 918750, 917750, 905250,
            904750, 925250, 921750, 914750, 906750, 913750, 922250, 911250,
            911750, 903750, 908750, 905750, 912250, 906250, 917250, 914250,
            907250, 918250, 916250, 910250, 910750, 907750, 924750, 909750,
            919750, 916750, 913250, 923750, 908250, 925750, 912750, 924250,
            921250, 920750, 922750, 902750, 923250 };

    private static final int ST817_V2 = 31;
    private static final int SD60RT = 33;

    public MyApplication() {
        allhtb = com.function.commfun.Sort(allhtb, allhtb.length);
        isconngoods = false;
        haveinit=false;

    }

    public class ReaderParams {

        // save param
        public int opant;

        public List<String> invpro;
        public String opro;
        public int[] uants;
        public int readtime;
        public int sleep;

        public int checkant;
        public int[] rpow;
        int crpow;
        public int[] wpow;

        public int region;
        public int[] frecys;
        public int frelen;

        public int session;
        public int qv;
        public int wmode;
        public int blf;
        public int maxlen;
        public int target;
        public int gen2code;
        public int gen2tari;

        public String fildata;
        public int filadr;
        public int filbank;
        public int filisinver;
        public int filenable;

        public int emdadr;
        public int emdbytec;
        public int emdbank;
        public int emdenable;

        public int antq;
        public int adataq;
        public int rhssi;
        public int invw;
        public int iso6bdeep;
        public int iso6bdel;
        public int iso6bblf;
        public int option;
        // other params

        public String password;
        public int optime;
        public void setdefaulpwval(int val)
        {
            crpow=val;
            rpow = new int[] { crpow, crpow, crpow, crpow, crpow, crpow, crpow,
                    crpow, crpow, crpow, crpow, crpow, crpow, crpow, crpow,
                    crpow };
            wpow = new int[] { crpow, crpow, crpow, crpow, crpow, crpow, crpow,
                    crpow, crpow, crpow, crpow, crpow, crpow, crpow, crpow,
                    crpow };
        }
        public ReaderParams() {
            opant = 1;
            invpro = new ArrayList<String>();
            invpro.add("GEN2");
            uants = new int[1];
            uants[0] = 1;
            sleep = 0;
            readtime = 50;
            optime = 1000;
            opro = "GEN2";
            checkant = 1;
            crpow=3000;
            // rpow=new
            // int[]{2700,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000};
            rpow = new int[] { crpow, crpow, crpow, crpow, crpow, crpow, crpow,
                    crpow, crpow, crpow, crpow, crpow, crpow, crpow, crpow,
                    crpow };
            wpow = new int[] { crpow, crpow, crpow, crpow, crpow, crpow, crpow,
                    crpow, crpow, crpow, crpow, crpow, crpow, crpow, crpow,
                    crpow };
            region = 1;
            frelen = 0;
            session = 0;
            qv = -1;
            wmode = 0;
            blf = 0;
            maxlen = 0;
            target = 0;
            gen2code = 2;
            gen2tari = 0;

            fildata = "";
            filadr = 32;
            filbank = 1;
            filisinver = 0;
            filenable = 0;

            emdadr = 0;
            emdbytec = 0;
            emdbank = 1;
            emdenable = 0;

            adataq = 0;
            rhssi = 1;
            invw = 0;
            iso6bdeep = 0;
            iso6bdel = 0;
            iso6bblf = 0;
            option = 0;
        }
    }


    public void initReader() {

                        // ST817_V2:31 SD60RT:33
                        boolean initResult = false;
                        for (int i = 0; i < 2; i++) {

                            RfidPower.PDATYPE PT = RfidPower.PDATYPE.valueOf(i == 0 ? ST817_V2 : SD60RT);
                            this.Rpower = new RfidPower(PT);
                            String serialPortName = Rpower.GetDevPath();
                            initResult = Rpower.PowerUp();
                            if (!initResult)
                                continue;
                         //  Toast.makeText(MyApplication.this, "上电:" + initResult, Toast.LENGTH_SHORT).show();
                            int portc = 1;
                            Reader.READER_ERR er = Mreader.InitReader_Notype(serialPortName, portc);
                            if (er == Reader.READER_ERR.MT_OK_ERR) {
                                Reader.Inv_Potls_ST ipst = Mreader.new Inv_Potls_ST();
                                ipst.potlcnt = 1;
                                ipst.potls = new Reader.Inv_Potl[ipst.potlcnt];
                                for (int index = 0; index < ipst.potlcnt; index++) {
                                    Reader.Inv_Potl ipl = Mreader.new Inv_Potl();
                                    ipl.weight = 30;
                                    ipl.potl = Reader.SL_TagProtocol.SL_TAG_PROTOCOL_GEN2;
                                    ipst.potls[index] = ipl;
                                }
                                er = Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_INVPOTL, ipst);
                                int[] av = new int[1];
                                Mreader.ParamGet(Reader.Mtr_Param.MTR_PARAM_READER_AVAILABLE_ANTPORTS,
                                        av);

                                antportc = portc;

                                setDefulatOutPower(30);
                                setDefulatOutPower(33);

                                return;
                            } else {

//				Toast.makeText(
//						this,
//						MyApplication.Constr_connectfialed + ":"
//								+ er.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }


    }

    /**
     * 设置模块初始输出功率
     *
     * @param dbi
     *            5-33/30
     */
    private void setDefulatOutPower(int dbi) {
        com.uhf.api.cls.Reader.AntPowerConf apcf =  Mreader.new AntPowerConf();
        apcf.antcnt = antportc;
        int[] rpow = new int[apcf.antcnt];
        int[] wpow = new int[apcf.antcnt];
        for (int i = 0; i < apcf.antcnt; i++) {
            com.uhf.api.cls.Reader.AntPower jaap = Mreader.new AntPower();
            jaap.antid = i + 1;
            jaap.readPower = (short) (100 * dbi);// 500-3000
            rpow[i] = jaap.readPower;

            jaap.writePower = (short) (100 * dbi);
            wpow[i] = jaap.writePower;
            apcf.Powers[i] = jaap;
        }

        try {
            com.uhf.api.cls.Reader.READER_ERR er = Mreader.ParamSet(com.uhf.api.cls.Reader.Mtr_Param.MTR_PARAM_RF_ANTPOWER,
                    apcf);
            if (er == com.uhf.api.cls.Reader.READER_ERR.MT_OK_ERR) {
                Rparams.rpow = rpow;
                Rparams.wpow = wpow;
            }else{

            }
        } catch (Exception e) {
            return;
        }
    }


}
