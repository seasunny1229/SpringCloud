package com.mooc.house.comment.common;

import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class BeanHelper {

    public static final String UPDATE_TIME_KEY = "updateTime";
    public static final String CREATE_TIME_KEY = "createTime";

    public static <T> void setDefaultProp(T target, Class<T> clazz){
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(clazz);
        for(PropertyDescriptor propertyDescriptor : descriptors){

            // get field name
            String fieldName = propertyDescriptor.getName();

            // get default value
            Object value  = null;
            try {
                value = PropertyUtils.getProperty(target, fieldName);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }

            // set default value
            if(String.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && value == null){
                try {
                    PropertyUtils.setProperty(target, fieldName, "");
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            else if(Number.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && value == null){
                try {
                    PropertyUtils.setProperty(target, fieldName, 0);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T> void onInsert(T target){
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        try {
            PropertyUtils.setProperty(target, CREATE_TIME_KEY, date);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
//        try {
//            PropertyUtils.setProperty(target, UPDATE_TIME_KEY, date);
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            e.printStackTrace();
//        }
    }

    public static <T> void onUpdate(T target){
        try {
            PropertyUtils.setProperty(target, UPDATE_TIME_KEY, System.currentTimeMillis());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }



}
