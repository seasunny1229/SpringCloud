package com.seasunny.test.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;

public class JDBCTest {

    private static final String DRIVER="com.mysql.cj.jdbc.Driver";
    private static final String URL="jdbc:mysql://47.103.192.187:3306/course";
    private static final String USERNAME="root";
    private static final String PASSWORD="123456";

    public static void main(String[] args)throws Exception{

        Connection conn=null;
        Statement statement=null;

        String sql="select * from chapter";

        try{
            Class.forName(DRIVER);
        }catch(ClassNotFoundException e){
            e.printStackTrace() ;
        }

        conn= DriverManager.getConnection(URL,USERNAME,PASSWORD);
        statement=conn.createStatement();
        statement.execute(sql);

        HashMap<String, String> map = new HashMap<>();

        ResultSet rSet = statement.getResultSet();
        if(rSet != null){
            while(rSet.next()){
                ResultSetMetaData metaData = rSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for(int i=0;i<columnCount;i++){
                    String columnName = metaData.getColumnName(i+1);
                    String data = rSet.getString(i+1);
                    map.put(columnName, data);
                }
            }

        }


        try{
            statement.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }



}

