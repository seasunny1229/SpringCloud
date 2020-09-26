package com.course.generator.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TestUtil {

    private static final String DEST_FILE_PATH = "libs12/src/main/java/com/course/generator/test/Test.java";
    private static final String SRC_DIR = "libs12/src/main/resources/ftl";

    public static void main(String... args) throws IOException, TemplateException {


        // freemarker configuration
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);

        // freemarker configuration params
        cfg.setDirectoryForTemplateLoading(new File(SRC_DIR));
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));

        // freemarker template
        Template template = cfg.getTemplate("test.ftl");

        // writer
        FileWriter fileWriter = new FileWriter(DEST_FILE_PATH);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        // data
        Map<String, Object> map = new HashMap<>();
        map.put("data1", "name");
        map.put("data2", "address");

        Person person = new Person();
        person.setName("bob");
        person.setPrimarySchoolName("hello world");
        map.put("data3", person);


        // process data to template
        template.process(map, bufferedWriter);

        // writer flush
        bufferedWriter.flush();
        fileWriter.close();
    }


}
