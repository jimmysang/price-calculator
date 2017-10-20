package com.util;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * Created by Sang on 15/11/2.
 */
public class JsonUtil {
    private static JsonConfig jsonConfig=new JsonConfig();

    public static Object stringToObject(String jsonStr,Class c){
        JSONObject o=JSONObject.fromObject(jsonStr);
        return JSONObject.toBean(o,c);
    }
}
