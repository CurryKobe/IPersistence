package com.sc.pojo;

/**
 * SQL 命令类型
 *
 * @author Clinton Begin
 */
public enum SqlCommandType {

    /**
     * 未知
     */
    UNKNOWN,
    /**
     * 插入
     */
    INSERT,
    /**
     * 更新
     */
    UPDATE,
    /**
     * 删除
     */
    DELETE,
    /**
     * 查询
     */
    SELECT,
    /**
     * FLUSH
     */
    FLUSH

}