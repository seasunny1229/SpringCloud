package com.course.generator.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerUtil {

    private static String ftlPath = "libs12/src/main/resources/ftl";

    private static Template template;

    public static void initConfig(String ftlName) throws IOException{

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);

        configuration.setDirectoryForTemplateLoading(new File(ftlPath));
        configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));

        template = configuration.getTemplate(ftlName);
    }

    public static void generator(String fileName, Map<String, Object> map) throws IOException, TemplateException{

        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        template.process(map, bufferedWriter);

        bufferedWriter.flush();
        fileWriter.close();
    }



}
