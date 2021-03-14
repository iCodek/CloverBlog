package com.clover.blog.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class ListTypeHandler extends BaseTypeHandler<List<String>> {

    public String listToString(List<String> list, char separator) {
        if (list.size() == 0) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < list.size(); i++) {
            sb.append('"');
            if (i == list.size()-1)
                sb.append(list.get(i)).append('"');
            else
                sb.append(list.get(i)).append('"').append(separator);
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> param, JdbcType jdbcType) throws SQLException {
//        ps.setString(i,String.valueOf(param.toString()));
        ps.setString(i,String.valueOf(listToString(param, ',')));
    }


    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        String result = rs.getString(columnName);
        if (null != result) {
            return JSONObject.parseArray(result).toJavaList(String.class);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        String result = rs.getString(columnIndex);
        if (null != result) {
            return JSONObject.parseArray(result).toJavaList(String.class);
        }
        return new ArrayList<>();
    }


    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String result = cs.getString(columnIndex);
        if (null != result) {
            return JSONObject.parseArray(result).toJavaList(String.class);
        }
        return new ArrayList<>();
    }
}
