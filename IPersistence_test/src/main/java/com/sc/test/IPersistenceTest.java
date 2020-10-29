package com.sc.test;

import com.sc.dao.IUserDao;
import com.sc.entity.User;
import com.sc.io.Resource;
import com.sc.sqlSession.SqlSession;
import com.sc.sqlSession.SqlSessionFactory;
import com.sc.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class IPersistenceTest {

    private IUserDao userDao;

    @Test
    public void test() throws Exception {
        InputStream input = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(input);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
       /* User user = new User();
        user.setId(1);
        user.setUsername("lucy");
        User u = sqlSession.selectOne("user.selectOne", user);

        System.out.println("u = " + u);

        List<User> users = sqlSession.selectList("user.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }*/

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        //代理对象调用接口中的任意方法，都会执行invoke方法
       List<User> userList = userDao.findAll();
        for (User user : userList) {
            System.out.println("user = " + user);
        }

        User user = new User();
        user.setId(1);
        user.setUsername("lucy");
        User u = userDao.findByCondition(user);
        System.out.println("单个：u = " + u);
    }

    @Before
    public void before() throws Exception {
        InputStream input = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(input);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        userDao = sqlSession.getMapper(IUserDao.class);
    }

    @Test
    public void saveTest() throws Exception {
        User user = new User();
        user.setId(11);
        user.setUsername("SC");
        user.setPassword("123");
        user.setBirthday("2020-06-28");

        userDao.insert(user);
    }

    @Test
    public void updateTest() throws Exception {
        User user = new User();
        user.setId(11);
        user.setUsername("SC_30");
        user.setPassword("123456");
        user.setBirthday("2020-06-08");

        userDao.update(user);
    }

    @Test
    public void deleteTest() throws Exception {
        User user = new User();
        user.setId(11);
        userDao.delete(user);
    }

}
