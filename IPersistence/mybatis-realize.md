1.提供配置信息（使用端）：
    sqlMapConfig.xml：存放数据源信息（可以引入mapper.xml获取其路径，可解决两次加载配置文件成字节流问题）
    Mapper.xml：存放sql语句的配置文件信息

框架端
2.读取配置文件
    读取配置后是以流的形式存放在内存中，不好操作，故可创建JavaBean来存储
        a.Configuration（存放sqlMapConfig.xml解析的信息）:存放数据库基本信息、Map<唯一标识,Mapper>唯一标识：namespace+"."+id
        b.MapperStatement（存放Mapper.xml解析的信息）:sql语句、statement类型、输入参数java类型、输出参数java类型

3.通过dom4j解析配置文件，将解析出来的内容封装到Configuration和MappedStatement中
    方法：SqlSessionFactory build():
        第一：使用dom4j解析配置文件，并将结果封装到Configuration和MapperStatement中
        第二：创建SqlSessionFactory对象；生成SqlSession：会话对象（这里使用了工厂模式）

4.创建SqlSessionFactory：
    方法：openSession()：获取sqlSession的实现类实例对象

5.创建SqlSession接口及实现类：主要封装CRUD方法
    方法：
        selectList(String StatementId, Object param)：查询所有
        selectOne(String StatementId, Object param)：查询单个
        close()：释放资源
        ......

6.创建Executor接口及实现类SimpleExecutor实现类
    Query(Configuration, MappedStatement, Object...params):执行JDBC代码


