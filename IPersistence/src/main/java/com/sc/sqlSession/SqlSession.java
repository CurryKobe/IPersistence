package com.sc.sqlSession;

import java.sql.SQLException;
import java.util.List;

public interface SqlSession {

    //查询所有
    public <E> List<E> selectList(String statementId, Object... params);


    //根据条件查询单个
    public <T> T selectOne(String statementId, Object... params);

    //为Dao生成代理实现类
    public <T> T getMapper(Class<?> mapperClass);


    boolean insert(String statement, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;

    int update(String statement, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;

    boolean delete(String statement, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;

}
