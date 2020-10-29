package com.sc.sqlSession;

import com.sc.config.BoundSql;
import com.sc.pojo.Configuration;
import com.sc.pojo.MappedStatement;
import com.sc.utils.GenericTokenParser;
import com.sc.utils.ParameterMapping;
import com.sc.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {


    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {
            //1、注册驱动，获取链接
            connection = configuration.getDataSource().getConnection();

            //2、获取sql语句  select * from user where id = #{id} and username = #{username}
            String sql = mappedStatement.getSql();

            //3、转化sql语句   select * from user where id = ? and username = ?
            BoundSql boundSql = getBoundSql(sql);

            //4、获取预处理对象：preparedStatement
            preparedStatement = connection.prepareStatement(boundSql.getSqlText());

            //5、设置参数
            //获取参数的全路径
            String paramType = mappedStatement.getParamType();
            Class<?> paramClass = getClassType(paramType);
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String content = parameterMapping.getContent();

                //反射
                Field declaredField = paramClass.getDeclaredField(content);
                //暴力访问
                declaredField.setAccessible(true);
                Object o = declaredField.get(params[0]);

                //设置参数
                preparedStatement.setObject(i+1, o);
            }

            //6、执行sql
            resultSet = preparedStatement.executeQuery();

            //7、封装返回结果集
            String resultType = mappedStatement.getResultType();
            Class<?> resultTypeClass = getClassType(resultType);
            ArrayList<Object> objects = new ArrayList<Object>();
            while (resultSet.next()) {
                Object o = resultTypeClass.newInstance();
                ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 1; i < metaData.getColumnCount(); i++) {
                    // 获取字段名
                    String columnName = metaData.getColumnName(i);
                    // 字段值
                    Object value = resultSet.getObject(columnName);
                    // 使用反射或者内省，根据数据库表和实体的对应关系，完成封装
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    writeMethod.invoke(o, value);
                }
                objects.add(o);
            }

            return (List<E>) objects;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class<?> getClassType(String paramsType) throws ClassNotFoundException {
        if (paramsType != null) {
            Class<?> aClass = Class.forName(paramsType);
            return aClass;
        }
        return null;
    }

    /**
     * 完成对#{}的解析工作
     *  1.将#{}使用?进行代替
     *  2.解析出#{}里面的值进行代替
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        //标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        System.out.println("解析后的sql：" + parseSql);
        //#{}里面解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(parseSql, parameterMappings);
        return boundSql;
    }

    @Override
    public boolean insert(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {

        boolean result = false;

        // 获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = preparedStatement(configuration, mappedStatement, params);

        // 5. 执行sql
        result = preparedStatement.execute();

        return result;
    }

    private PreparedStatement preparedStatement(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException {

        // 注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();

        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);


        String paramterType = mappedStatement.getParamType();
        Class<?> paramtertypeClass = getClassType(paramterType);

        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String content = parameterMapping.getContent();

            //反射
            Field declaredField = paramtertypeClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);

            preparedStatement.setObject(i+1,o);

        }

        return preparedStatement;

    }

    @Override
    public int update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, SQLException {

        int result = 0;

        // 获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = preparedStatement(configuration, mappedStatement, params);

        // 5. 执行sql
        result = preparedStatement.executeUpdate();

        return result;
    }

    @Override
    public boolean delete(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        boolean result = false;

        // 获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = preparedStatement(configuration, mappedStatement, params);

        // 5. 执行sql
        result = preparedStatement.execute();

        return result;
    }
}
