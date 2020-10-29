package com.sc.config;

import com.sc.utils.ParameterMapping;

import java.util.ArrayList;
import java.util.List;

public class BoundSql {

    private String sqlText;//解析后的sql
    private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();

    public BoundSql() {
    }

    public BoundSql(String sqlText, List<ParameterMapping> parameterMappings) {
        this.sqlText = sqlText;
        this.parameterMappings = parameterMappings;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }
}
