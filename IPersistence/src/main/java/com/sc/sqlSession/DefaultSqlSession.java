package com.sc.sqlSession;

import com.sc.pojo.Configuration;
import com.sc.pojo.MappedStatement;
import com.sc.pojo.SqlCommandType;

import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) {
        //完成对simpleExecutor里query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        List<Object> list = simpleExecutor.query(configuration, configuration.getMappedStatementMap().get(statementId), params);
        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) {
        List<Object> objects = selectList(statementId, params);
        if (objects.size()==1) {
            return (T) objects.get(0);
        } else {
            throw new RuntimeException("查询结果为空或者结果过多");
        }
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        //使用JDK动态代理来为Dao接口生成代理对象并返回
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
               /**
                 * proxy：当前代理对象的应用
                 * method:当前被调用方法的引用
                 * args：传递的参数
                 */
                //底层还是去执行JDBC代码，准备参数：statementId--sql语句唯一标识--namespace.id=接口全限定名.方法名(namespace跟对应的接口路径一致，sqlId跟接口方法名一致)
                //准备参数1：statementId
                String methodName = method.getName();//方法名
                String className = method.getDeclaringClass().getName();//方法所在类的全限定名
                String statementId = className + "." + methodName;

                // 获取statementId对应的标签
                SqlCommandType sqlCommandType = configuration.getMappedStatementMap().get(statementId).getSqlCommandType();

                // 根据标签执行对应的操作
                switch (sqlCommandType) {
                    case INSERT: {
                        return insert(statementId,args);
                    }
                    case UPDATE: {
                        return update(statementId,args);
                    }
                    case DELETE: {
                        return delete(statementId,args);
                    }
                    case SELECT: {
                        //准备参数2：params：args
                        //根据返回值类型选择调用selectList or selectOne
                        //获取被调用方法的返回值类型
                        Type genericReturnType = method.getGenericReturnType();
                        //判断是否进行了 泛型类型参数化
                        if (genericReturnType instanceof ParameterizedType) {
                            List<Object> objects = selectList(statementId, args);
                            return objects;
                        }
                        return selectOne(statementId, args);
                    }
                }
                throw new Exception("未找到相关标签，执行错误");
            }
        });
        return (T) proxyInstance;
    }

    @Override
    public boolean insert(String statementId, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        boolean result;
        //将要去完成对simpleExecutor里的query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        result = simpleExecutor.insert(configuration, mappedStatement, params);
        return result;
    }

    @Override
    public int update(String statementId, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        int result;
        //将要去完成对simpleExecutor里的query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        result = simpleExecutor.update(configuration, mappedStatement, params);
        return result;
    }

    @Override
    public boolean delete(String statementId, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        boolean result;
        //将要去完成对simpleExecutor里的query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        result = simpleExecutor.delete(configuration, mappedStatement, params);
        return result;
    }

}
