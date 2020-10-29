package com.sc.config;

import com.sc.pojo.Configuration;
import com.sc.pojo.MappedStatement;
import com.sc.pojo.SqlCommandType;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {

    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");

        List<Element> list = rootElement.elements();
        for (Element element : list) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String paramterType = element.attributeValue("paramterType");
            String sqlText = element.getTextTrim();

            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParamType(paramterType);
            mappedStatement.setSql(sqlText);

            //获取标签
            String elementName = element.getName();
            System.out.println("标签名："+elementName);
            mappedStatement.setSqlCommandType(SqlCommandType.valueOf(elementName.toUpperCase()));

            String key = namespace + "." + id;
            configuration.getMappedStatementMap().put(key, mappedStatement);

        }




    }

}
