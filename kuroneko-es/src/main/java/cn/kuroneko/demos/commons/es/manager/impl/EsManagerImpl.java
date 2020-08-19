package cn.kuroneko.demos.commons.es.manager.impl;

import cn.kuroneko.demos.commons.es.config.JestConfig;
import cn.kuroneko.demos.commons.es.commons.ResultCode;
import cn.kuroneko.demos.commons.es.manager.EsManager;
import cn.kuroneko.demos.commons.exception.KuronekoException;
import cn.kuroneko.demos.commons.utils.VerifyUtils;
import com.google.gson.JsonObject;
import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.GetMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;

/**
 * @author liwei
 * @date 2019/12/28 4:44 PM
 */
@SuppressWarnings("Duplicates")
@Component
@Slf4j
public class EsManagerImpl implements EsManager {
    private static final String INDEX_NOT_FOUND = "index_not_found_exception";
    private static final int ES_CODE_NOT_FOUND = 404;
    @Autowired
    private JestClient jestClient;

    @PostConstruct
    public synchronized void init() throws KuronekoException {
        //启动时， 先做创建群发目标用户的索引动作
        this.createMassTargetUserIndex();
    }

    @Override
    public void createMassTargetUserIndex() throws KuronekoException {
        createMassIndex(JestConfig.MASS_USERS_INDEX, JestConfig.MASS_USER_MAPPING);
        createMassIndex(JestConfig.MASS_SUMMARY_INDEX, JestConfig.MASS_SUMMARY_MAPPING);
    }

    private void createMassIndex(String indexName, String mapping) throws KuronekoException {
        //查询该index是否存在
        boolean exist = indexExists(indexName);
        if (exist) {
            return;
        }
        CreateIndex createIndex = new CreateIndex.Builder(indexName).mappings(mapping).build();

        JestResult result = execute(createIndex);

        if (log.isDebugEnabled()) {
            log.debug("index {} created. result:{}", result.getJsonString());
        }
    }

    @Override
    public boolean indexExists(String indexName) throws KuronekoException {
        VerifyUtils.notBlank(indexName, "EsManagerImpl.indexExists#indexName should not be blank.", true);
        GetMapping getMapping = new GetMapping.Builder().addIndex(indexName).build();

        JestResult result = null;
        try {
            result = execute(getMapping);
        } catch (KuronekoException e) {
            if (StringUtils.contains(e.getMessage(), INDEX_NOT_FOUND)) {
                return false;
            }
            throw e;
        }
        JsonObject jo = result.getJsonObject();
        return Objects.nonNull(jo) && Objects.nonNull(jo.get(indexName));
    }

    @Override
    public boolean isSuccess(JestResult result) {
        return Objects.nonNull(result) && result.isSucceeded();
    }

    @Override
    public void saveInfo(String indexName, String type, String id, Object docObject) throws KuronekoException {
        VerifyUtils.notBlank(indexName, "EsManagerImpl.saveInfo#indexName", true);
        VerifyUtils.notBlank(type, "EsManagerImpl.saveInfo#type", true);
        VerifyUtils.notNull(docObject, "EsManagerImpl.saveInfo#docObject", true);

        Index.Builder builder = new Index.Builder(docObject).index(indexName).type(type);
        if (StringUtils.isNotBlank(id)) {
            builder.id(id);
        }

        execute(builder.build());
    }

    @Override
    public JestResult queryDocById(String indexName, String type, String id) throws KuronekoException {
        VerifyUtils.notBlank(indexName, "EsManagerImpl.queryDocById#indexName", true);
        VerifyUtils.notBlank(type, "EsManagerImpl.queryDocById#type", true);
        VerifyUtils.notNull(id, "EsManagerImpl.queryDocById#docObject", true);
        // JestResult result = this.execute(1,1,indexName,type,()-> )
        Get.Builder builder = new Get.Builder(indexName, id).type(type);
        return executeReturnNullIfAbsent(builder.build());
    }

    @Override
    public JestResult deleteDocById(String indexName, String type, String id) throws KuronekoException {
        if (StringUtils.isBlank(indexName) || StringUtils.isBlank(type) || StringUtils.isBlank(id)) {
            log.warn("None document is deleted because of blank among indexName[{}], type[{}] and id[{}]", indexName,
                    type, id);
            return null;
        }
        Delete.Builder builder = new Delete.Builder(id).index(indexName).type(type);
        return executeReturnNullIfAbsent(builder.build());
    }

    @Override
    public <T> T parseSingleSourceObject(JestResult result, Class<T> clazz) {
        if (Objects.isNull(result)) {
            return null;
        }

        return result.getSourceAsObject(clazz, false);
    }

    /**
     * 执行ES的语句， 若找不到数据则返回null. 如删除一个不存在的数据， 或者查找一个不存在的数据时， 将返回null
     *
     * @param action
     * @return
     * @throws KuronekoException
     */
    private JestResult executeReturnNullIfAbsent(Action action) throws KuronekoException {
        JestResult result = null;
        try {
            result = execute(action);
        } catch (KuronekoException e) {
            if (StringUtils.equals(e.getOriginalCode(), ResultCode.SEARCH_ES_DOC_NOT_FOUND.code)) {
                return null;
            }
            throw e;
        }
        return result;
    }

    /**
     * 执行es 语句. 仅在执行成功时返回
     *
     * @param action
     * @return
     * @throws KuronekoException
     */
    private JestResult execute(Action action) throws KuronekoException {
        String res = StringUtils.EMPTY;
        JestResult result = null;
        try {
            result = jestClient.execute(action);
            if (isSuccess(result)) {
                res = result.getSourceAsString();
            } else {
                KuronekoException ve = new KuronekoException(ResultCode.ACCESS_ES_FAILED.code,
                        ResultCode.ACCESS_ES_FAILED.msg + ": " + result.getErrorMessage());

                if (Objects.isNull(result.getJsonObject()) || StringUtils
                        .contains(result.getErrorMessage(), INDEX_NOT_FOUND)) {
                    throw ve;
                }

                boolean notFound = (result.getResponseCode() == ES_CODE_NOT_FOUND)
                        || (Objects.nonNull(result.getJsonObject().get("found"))
                        && !result.getJsonObject().get("found").getAsBoolean());

                if (notFound) {
                    throw new KuronekoException(ResultCode.SEARCH_ES_DOC_NOT_FOUND, action.toString());
                }

                throw ve;
            }
        } catch (IOException e) {
            throw new KuronekoException(ResultCode.ACCESS_ES_FAILED, action.toString(), e);
        }

        return result;
    }
}
