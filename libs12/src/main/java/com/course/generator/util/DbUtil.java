package com.course.generator.util;

import com.course.generator.enums.EnumGenerator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbUtil {

    // connect to database
    public static Connection getConnection(){

        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://47.103.192.187:3306/dev_course?useSSL=false";
            String username = "root";
            String password = "123456";

            connection = DriverManager.getConnection(url, username, password);

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }

        return connection;
    }

    // get Chinese table name from table comment
    public static String getTableComment(String tableName) throws Exception{

        // database connection
        Connection connection = getConnection();

        // statement
        Statement statement = connection.createStatement();

        // result set
        ResultSet rs = statement.executeQuery("select table_comment from information_schema.tables where table_name = '" + tableName + "'");

        // get table comment (Chinese table name)
        String tableNameCH = "";
        if(rs != null){
            while (rs.next()){
                tableNameCH = rs.getString("table_comment");
                break;
            }
        }

        // close resource
        rs.close();
        statement.close();
        connection.close();

        return tableNameCH;
    }

    // get column info from database
    public static List<Field> getColumnByTableNames(String tableName) throws Exception{

        List<Field> fieldList = new ArrayList<>();

        // connection
        Connection connection = getConnection();

        // statement
        Statement statement = connection.createStatement();

        // result set
        ResultSet resultSet = statement.executeQuery("show full columns from " + tableName);
        if(resultSet != null){
            while (resultSet.next()){

                // id,name......
                String columnName = resultSet.getString("Field");

                // int
                String type = resultSet.getString("Type");

                // comment
                String comment = resultSet.getString("Comment");

                // YES, NO
                String nullAble = resultSet.getString("Null");

                Field field = new Field();

                // name
                field.setName(columnName);
                field.setNameHump(lineToHump(columnName));
                field.setNameBigHump(lineToBigHump(columnName));

                // database type
                // java type
                field.setType(type);
                field.setJavaType(sqlTypeToJavaType(type));

                // comment
                // Chinese name
                field.setComment(comment);
                if(comment.contains("|")){
                    field.setNameCn(comment.substring(0, comment.indexOf("|")));
                }
                else {
                    field.setNameCn(comment);
                }

                // nullAble
                field.setNullAble("YES".equals(nullAble));

                // length
                if(type.toUpperCase().contains("varchar".toUpperCase())){
                    String lengthStr = type.substring(type.indexOf("(") + 1, type.length() - 1);
                    field.setLength(Integer.valueOf(lengthStr));
                }
                else {
                    field.setLength(0);
                }

                // enums
                if(comment.contains("枚举")){
                    field.setEnums(true);

                    // [CourseLevelEnum] to get enum info
                    int start = comment.indexOf("[");
                    int end = comment.indexOf("]");
                    String enumsName = comment.substring(start + 1, end);
                    String enumsConst = EnumGenerator.toUnderLine(enumsName);
                    field.setEnumConst(enumsConst);
                }
                else {
                    field.setEnums(false);
                }

                fieldList.add(field);
            }

            resultSet.close();
        }

        // close resources
        statement.close();
        connection.close();

        return fieldList;
    }

    // SQL type tp Java type
    public static String sqlTypeToJavaType(String sqlType){
        if(sqlType.toUpperCase().contains("varchar".toUpperCase())
            || sqlType.toUpperCase().contains("char".toUpperCase())
            || sqlType.toUpperCase().contains("text".toUpperCase())
        ){
            return "String";
        }
        else if(sqlType.toUpperCase().contains("datetime".toUpperCase())){
            return "Date";
        }
        else if(sqlType.toUpperCase().contains("int".toUpperCase())){
            return "Integer";
        }
        else if(sqlType.toUpperCase().contains("long".toUpperCase())){
            return "Long";
        }
        else if(sqlType.toUpperCase().contains("decimal".toUpperCase())){
            return "BigDecimal";
        }
        else {
            return "String";
        }


    }

    // member_course --> memberCourse
    public static String lineToHump(String string){

        // pattern
        Pattern linePattern = Pattern.compile("_(\\w)");

        // lowercase string input
        string = string.toLowerCase();

        // matcher
        Matcher matcher = linePattern.matcher(string);

        // string buffer append
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()){
            matcher.appendReplacement(stringBuffer, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

    // member_course --> MemberCourse
    public static String lineToBigHump(String string){
        String s = lineToHump(string);
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }


}
