package com.imooc.miaoshaproject.test;

import java.util.Scanner;

public class Test3 {

    public static void main(String... args){

        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        System.out.println(isSatisfied(input)?"YES":"NO");
        scanner.close();

    }


    private static boolean isSatisfied(String input){
        for(int i=0;i<input.length()/2;i++){
            if(input.charAt(i) != input.charAt(input.length()-1-i)){
                return false;
            }
        }
        return true;
    }




}
