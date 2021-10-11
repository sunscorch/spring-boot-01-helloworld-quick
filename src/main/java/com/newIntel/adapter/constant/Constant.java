package com.newIntel.adapter.constant;

import java.time.ZoneId;
import java.util.*;

public class Constant {
    //  // 设备功能码:1:遥测
    public static final int FUNCTION_CODE1 = 1;
    // functionCode:遥设
    public static final int FUNCTION_CODE2 = 2;

    public static final String DEVICE_CLOSE = "0";
    public static final String DEVICE_ON = "1";
    public static final String DEVICE_OFFLINE = "2";

    /*
    public static final int WATER_PUMP_CLOSE = 0;
    public static final int WATER_PUMP_ON = 1;
    public static final int WATER_PUMP_OFFLINE = 2;
    */
    public static final int SENSOR_NORMAL = 1;
    public static final int SENSOR_ABNORMAL = 0;

    public static final int WIND_MACHINE_CODE = 1;
    public static final int WATER_PUMP_CODE = 2;

    public static final int CONTROL_CLOSE = 0;
    public static final int CONTROL_ON = 1;
    public static final int DEVICE_FAIL = 2;

    //水位
    public static final int WATER_LEVEL_LOW = 0;
    public static final int WATER_LEVEL_MID = 1;
    public static final int WATER_LEVEL_HIGH = 2;

    //
    //CO：;整数型.
    public static final String CO_TYPE_CODE = "11";
    //CH4：;整数型
    public static final String CH4_TYPE_CODE = "12";
    //H2S：;整数型
    public static final String H2S_TYPE_CODE = "13";
    //O2：;氧气浓度,精确到小数点后两位
    public static final String O2_TYPE_CODE = "14";
    //环境温度：;精确到小数点后两位
    public static final String TEMP_TYPE_CODE = "15";
    //环境湿度：;精确到小数点后两位
    public static final String HUMIDITY_TYPE_CODE = "16";

    public static final String WATERPUMP_OUTPUT_TYPE_CODE = "21";

    public static final String WATERPUMP_INPUT_TYPE_CODE = "22";

    //ITC 电话
    public static final String WATERPUMP_ITC_CODE = "60";


    public static final String MAXIEW_SWITCH_STATUS_ON = "true";
    public static final String MAXIEW_SWITCH_STATUS_OFF = "false";

    public static final String TOPSERVER_ITC_ON = "ONLINE";
    public static final String TOPSERVER_ITC_OFF = "OFFLINE";

    public static final Long staleThreshold = 5L;

    public static final String PHONE_ANSWER = "ANSWER";

    public static final String PHONE_NO_ANSWER = "NOANSWER";

    public static final String PHONE_BUSY = "BUSY";

    public static final String PHONE_CANCEL = "CANCEL";

    public static final String FIRE_BALL_OUTPUT = "53";

    //public static final String[] FIRE_OBJ_LIST = new String[]{"1301530101", "1301530102", "1301530103", "1301530104", "1301530105", "1301530106", "1301530107", "1301530108", "1301530109", "1301530110", "1301530111", "1301530112", "1301530113", "1301530114", "1301530115", "1301530116", "1301530117", "1301530118", "1301530119", "1301530120", "1301530121", "1301530122", "1301530123", "1301530124", "1301530125", "1301530126", "1301530127", "1301530128", "1301530129", "1301530130", "1301540101", "1301540102", "1301540103", "1301540104", "1301540105", "1301540106", "1301540107", "1301540108", "1301540109", "1301540110", "1301540111", "1301540112", "1301540113", "1301540114", "1301540115", "1301540116", "1301540117", "1301540118", "1301540119", "1301540120", "1301540121", "1301540122", "1301540123", "1301540124", "1301540125", "1301540126", "1301540127", "1301540128", "1301540129", "1301540130", "1301560101", "1301560102", "1301560103", "1301560104", "1301560105", "1301560106", "1301560107", "1301560108", "1301560109", "1301560110", "1301560111", "1301560112", "1301560113", "1301560114", "1301560115", "1301560116", "1301560117", "1301560118", "1301560119", "1301560120", "1301560121", "1301560122", "1301560123", "1301560124", "1301560125", "1301560126", "1301560127", "1301560128", "1301560129", "1301560130"};

    public static final String[] FIRE_OBJ_LIST = new String[]{"1301530101","1301560130"};
    public static final SortedSet<String> FIRE_OBJ_SET = new TreeSet<>(Arrays.asList(FIRE_OBJ_LIST));
    public static final String[] PHONE_OBJ_LIST = new String[]{"1301600101", "1302600202", "1303600303","1304600404", "1305600505", "1306600606", "1307600707", "1308600808", "1309600909", "1310601010", "1311601111", "1312601212", "1313601313", "1314601414", "1315601515", "1316601616", "1317601717", "1318601818", "1319601919", "1320602020", "1321602121", "1322602222", "1323602323", "1324602424", "1325602525", "1326602626", "1327602727", "1328602828"};
    public static final SortedSet<String> PHOPNE_OBJ_SET = new TreeSet<>(Arrays.asList(PHONE_OBJ_LIST));
    public static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Shanghai");
}