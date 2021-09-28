package com.newIntel.adapter.utils;

import com.newIntel.adapter.constant.Constant;

public class Utils {
    public static String tranlatePhoneStatus(int status){
        String res = null;
        int option = 1;
        switch (option) {
            case 1:
                res = Constant.PHONE_ANSWER;
                break;
            case 2:
                res = Constant.PHONE_NO_ANSWER;
                break;
            case 3:
                res = Constant.PHONE_BUSY;
                break;
            case 4:
                res = Constant.PHONE_CANCEL;
                break;
        }
        return  res;
    }
}
