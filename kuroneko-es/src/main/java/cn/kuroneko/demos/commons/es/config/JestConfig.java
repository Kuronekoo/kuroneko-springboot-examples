package cn.kuroneko.demos.commons.es.config;

import cn.kuroneko.demos.commons.proxy.KuronekoDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @author liwei
 * @date 2019/9/17 7:15 PM
 */
@Configuration
public class JestConfig {
    private static final int LOCAL_LOCK_COUNT = 1111;

    /**
     * 群发消息目标用户索引
     */
    public static final String MASS_USERS_INDEX = "vwop_idx_pub_conf_mass_target_user";
    public static final String MASS_USERS_TYPE = "user";
    public static final String MASS_USER_MAPPING =
            "{\"user\": {\"properties\": {\"massConfId\": {\"type\": \"keyword\"}, \"completeMassTime\": {\"type\": \"date\", \"format\": \"yyyy-MM-dd HH:mm:ss\"}}}}";

    public static final String MASS_SUMMARY_INDEX = "vwop_idx_pub_conf_mass_target_user_summary";
    public static final String MASS_SUMMARY_TYPE = "summary";
    public static final String MASS_SUMMARY_MAPPING =
            "{\"summary\": {\"properties\": {\"logicDocId\": {\"type\": \"keyword\"}, \"totalCnt\": {\"type\": \"integer\"}, \"totalPage\": {\"type\": \"integer\"}}}}";

    @Bean
    public Gson gson(GsonBuilder gsonBuilder) {
        return gsonBuilder
                .registerTypeAdapter(Date.class, new KuronekoDateTypeAdapter(LOCAL_LOCK_COUNT))
                .create();
    }

}
