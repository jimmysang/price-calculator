package com.util;

/**
 * Created by Sang on 15/10/30.
 */
public class StringUtil {
    public static int toInt(String s){
        try {
            return Integer.parseInt(s);
        }catch (Exception e){
            return 0;
        }
    }
}
