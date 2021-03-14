package com.imooc.miaoshaproject.test;

public class Test8 {


    public static void main(String... args){

        // 设置两个字符串类型的整数
        String num1 = "12345";
        String num2 = "2345678";
        System.out.println(add(num1, num2));


        String num3 = "99999999";
        String num4 = "99999999";
        System.out.println(add(num3, num4));

        String num5 = "99";
        String num6 = "99";
        System.out.println(add(num5, num6));



    }


    private static String add(String num1, String num2){

        // 进位标志
        int add = 0;

        // 输出的字符串
        String output = "";


        // 遍历，从个位开始，取两个数对应的位进行加法，同时加上进位，保存在字符串中
        // 两个数位数判断，取大的数
        int length = num1.length() > num2.length() ? num1.length() : num2.length();
        for(int i=0;i<length;i++){

            int number1 = 0;
            if(i <num1.length()){
                number1 = Integer.valueOf(num1.substring(num1.length()-1-i, num1.length()-i));
            }

            int number2 = 0;
            if(i< num2.length()){
                number2 = Integer.valueOf(num2.substring(num2.length()-1-i, num2.length()-i));
            }

            int sum = number1 + number2 + add;

            // 判断是否大于等于10
            if(sum >= 10){
                add = 1;
                output = sum%10 + output;
            }
            else {
                add = 0;
                output = sum + output;
            }
        }

        // 最后的进位处理
        if(add>0){
            output = add + output;
        }

        return output;
    }

}
