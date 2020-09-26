package com.course.generator.server;

import com.course.generator.util.DbUtil;
import com.course.generator.util.Field;
import com.course.generator.util.FreemarkerUtil;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServerGenerator {

    private static String MODULE = "system";
    private static String PACKAGE = "libs15";

    private static String toDtoPath = PACKAGE + "/src/main/java/com/course/" + MODULE +  "/dto/";

    private static String toServicePath = PACKAGE + "/src/main/java/com/course/" + MODULE +  "/service/";

    private static String toControllerPath = PACKAGE + "/src/main/java/com/course/" + MODULE +  "/controller/admin/";

    private static String generatorConfigPath = PACKAGE +  "/src/main/resources/generator/generatorConfig.xml";


    public static void main(String[] args) throws Exception{

        // set module
        String module = MODULE;

        // read generator config file
        File file = new File(generatorConfigPath);

        // sax reader
        SAXReader saxReader = new SAXReader();

        // read file
        Document document = saxReader.read(file);

        // get root element
        Element rootElement = document.getRootElement();

        // get sub "context" element
        Element contextElement = rootElement.element("context");

        // get first sub "table" element
        Element tableElement = contextElement.elementIterator("table").next();

        // tableName = "member_course"
        // domainObjectName (Domain) = "MemberCourse"
        String Domain = tableElement.attributeValue("domainObjectName");
        String tableName = tableElement.attributeValue("tableName");

        // tableNameCn is Chinese name
        String tableNameCn = DbUtil.getTableComment(tableName);

        // domain = "memberCourse"
        String domain = Domain.substring(0,1).toLowerCase() + Domain.substring(1);

        // get field info
        List<Field> fieldList = DbUtil.getColumnByTableNames(tableName);

        // get java type set
        Set<String> typeSet = getJavaTypes(fieldList);

        // model map
        Map<String, Object> map = new HashMap<>();
        map.put("Domain", Domain);
        map.put("domain", domain);
        map.put("tableNameCn", tableNameCn);
        map.put("module", module);
        map.put("fieldList", fieldList);
        map.put("typeSet", typeSet);

        // dto
        FreemarkerUtil.initConfig("dto.ftl");
        FreemarkerUtil.generator(toDtoPath + Domain + "Dto.java", map);

        // service
        FreemarkerUtil.initConfig("service.ftl");
        FreemarkerUtil.generator(toServicePath + Domain + "Service.java", map);

        // controller
        FreemarkerUtil.initConfig("controller.ftl");
        FreemarkerUtil.generator(toControllerPath + Domain + "Controller.java", map);

    }

    // get all Java types
    private static Set<String> getJavaTypes(List<Field> fieldList){

        Set<String> set = new HashSet<>();

        for(int i=0;i<fieldList.size();i++){
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }

        return set;
    }




}
