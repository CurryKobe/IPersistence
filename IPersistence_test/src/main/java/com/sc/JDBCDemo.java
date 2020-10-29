package com.sc;

import com.sc.entity.User;

import java.sql.*;

public class JDBCDemo {

    private static User user = new User();

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            //加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            //通过驱动管理类获取数据库链接
            connection = DriverManager.getConnection("jdbc://mysql://localhost:3306/mybatis_details?characterEncoding=utf-8",
                    "root", "1234");
            //定义sql语句 ？表示占位符
            String sql = "select * from user where id = ?";
            //获取预处理statement
            preparedStatement = connection.prepareStatement(sql);
            //设置参数
            preparedStatement.setString(1, "1");
            //向数据库发出sql执行查询，查询出结果集
            resultSet = preparedStatement.executeQuery();
            //遍历结果集
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                //封装User
                user.setId(id);
                user.setUsername(username);
            }
            System.out.println(user);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //重点：释放资源
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
