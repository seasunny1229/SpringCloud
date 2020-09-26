package com.course.business.util;

import java.util.UUID;

public class UuidUtil {

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };


    public static String getShortUuid(){
        StringBuffer stringBuffer = new StringBuffer();
        String uuid = getUuid();
        for(int i=0;i<8;i++){
            String str = uuid.substring(i*4, i*4+4);
            int x = Integer.parseInt(str, 16);
            stringBuffer.append(chars[x % 0x3E]);
        }
        return stringBuffer.toString();
    }

    public static String getUuid(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-","");
    }

    public static void main(String[] args) {
        System.out.println(getShortUuid());
    }


}