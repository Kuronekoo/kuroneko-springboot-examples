package cn.kuroneko.demos.commons.utils;

import cn.kuroneko.demos.commons.proxy.KuronekoDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * @author liwei
 * @date 2019/8/22 3:24 PM
 */
public class GsonHelper {
    private static final int LOCAL_LOCK_COUNT = 1111;

    public final static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Date.class, new KuronekoDateTypeAdapter(LOCAL_LOCK_COUNT))
            // .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .create();
    // public final static Gson GSON_STAY_NULLS = new GsonBuilder()
    //         .registerTypeAdapter(Date.class, new KuronekoDateTypeAdapter(LOCAL_LOCK_COUNT))
    //         .serializeNulls()
    //         .create();

    public static void main(String[] args) {
        String json = "[\"alice\",\"catalina\"]";
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> list = GSON.fromJson(json, type);
        System.out.println(list);
    }
}
