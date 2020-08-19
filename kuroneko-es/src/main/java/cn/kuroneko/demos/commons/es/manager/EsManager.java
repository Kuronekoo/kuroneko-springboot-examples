package cn.kuroneko.demos.commons.es.manager;

import cn.kuroneko.demos.commons.exception.KuronekoException;
import io.searchbox.client.JestResult;

/**
 * 操作 elasticsearch API
 *
 * @author liwei
 * @date 2019/12/28 4:20 PM
 */
public interface EsManager {

    /**
     * 创建群发目标用户索引
     *
     * @throws KuronekoException
     */
    void createMassTargetUserIndex() throws KuronekoException;

    /**
     * 判断索引是否已存在
     *
     * @param indexName 索引名称
     * @return true: 索引已存在
     * @throws KuronekoException
     */
    boolean indexExists(String indexName) throws KuronekoException;

    /**
     * 判断返回是否成功
     *
     * @param result
     * @return
     */
    boolean isSuccess(JestResult result);

    /**
     * 保存数据到es
     *
     * @param indexName 索引名称
     * @param type      类型
     * @param id        es中文档的ID。可不填
     * @param docObject 待写入es的对象
     * @throws KuronekoException
     */
    void saveInfo(String indexName, String type, String id, Object docObject) throws KuronekoException;

    /**
     * 根据_id 查找数据
     *
     * @param indexName
     * @param type
     * @param id
     * @return
     * @throws KuronekoException
     */
    JestResult queryDocById(String indexName, String type, String id) throws KuronekoException;

    /**
     * 根据_id 删除数据
     *
     * @param indexName
     * @param type
     * @param id
     * @return 删除成功则返回非null JestResult, 未找到删除目标则返回null
     */
    JestResult deleteDocById(String indexName, String type, String id) throws KuronekoException;

    /**
     * 取es查询结果中的source 字段 ， 并反序列化
     *
     * @param result
     * @param clazz
     * @param <T>
     * @return result不为SearchResult的子类时， 则返回null
     */
    <T> T parseSingleSourceObject(JestResult result, Class<T> clazz);

}
