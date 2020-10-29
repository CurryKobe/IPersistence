package com.sc.dao;

import com.sc.entity.User;
import com.sc.io.Resource;
import com.sc.sqlSession.SqlSession;
import com.sc.sqlSession.SqlSessionFactory;
import com.sc.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements IUserDao {

    public List<User> findAll() throws PropertyVetoException, DocumentException {
        InputStream input = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(input);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession.selectList("user.selectList");
    }

    public User findByCondition(User user) throws PropertyVetoException, DocumentException {
        InputStream input = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(input);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession.selectOne("user.selectOne", user);
    }

    public boolean insert(User user) throws PropertyVetoException, DocumentException, ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException {
        InputStream input = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(input);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession.insert("user.insert", user);
    }

    public int update(User user) throws PropertyVetoException, DocumentException, ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException {
        InputStream input = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(input);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession.update("user.update", user);
    }

    public boolean delete(User user) throws PropertyVetoException, DocumentException, ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException {
        InputStream input = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(input);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession.delete("user.delete", user);
    }


}
