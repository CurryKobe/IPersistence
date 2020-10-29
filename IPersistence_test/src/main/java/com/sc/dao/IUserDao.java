package com.sc.dao;

import com.sc.entity.User;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.List;

public interface IUserDao {

    //查询所有用户
    public List<User> findAll() throws PropertyVetoException, DocumentException;

    //查询单个
    public User findByCondition(User user) throws PropertyVetoException, DocumentException;

    //新增
    public boolean insert(User user) throws PropertyVetoException, DocumentException, ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException;

    //更新
    public int update(User user) throws PropertyVetoException, DocumentException, ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException;

    //删除
    public boolean delete(User user) throws PropertyVetoException, DocumentException, ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException;
}
