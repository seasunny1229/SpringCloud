package com.seasunny.test.controller;

import com.seasunny.test.Result;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/access")
public class JDBCController {

    private static final String DRIVER="com.mysql.cj.jdbc.Driver";
    private static final String URL="jdbc:mysql://47.103.192.187:3306/course";
    private static final String USERNAME="root";
    private static final String PASSWORD="123456";


    @PostMapping("/get")
    public Result get(@RequestBody String sql) throws Exception{


        Connection conn;
        Statement statement;

        if(sql == null){
            return null;
        }

        try{
            Class.forName(DRIVER);
        }catch(ClassNotFoundException e){
            e.printStackTrace() ;
        }

        conn= DriverManager.getConnection(URL,USERNAME,PASSWORD);
        statement=conn.createStatement();

        long startTime = System.currentTimeMillis();
        statement.execute(sql);

        Result result = new Result();
        ResultSet rSet = statement.getResultSet();
        if(rSet != null){

            ResultSetMetaData metaData = rSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<String> fields = new ArrayList<>();
            for(int i=0;i<columnCount;i++){
                String columnName = metaData.getColumnName(i+1);
                fields.add(columnName);
            }
            result.setFields(fields);

            List<String[]> dataList = new ArrayList<>();
            while(rSet.next()){
                String[] value = new String[columnCount];
                for(int i=0;i<columnCount;i++){
                    String data = rSet.getString(i+1);
                    value[i] = data;
                }
                dataList.add(value);
            }
            result.setData(dataList);
        }

        long duration = System.currentTimeMillis() - startTime;

        result.setDuration(duration);

        try{
            statement.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }



}
