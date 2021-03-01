package com.imooc.miaoshaproject.test;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Test1 {

    public static void main(String... args){

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String inputString = scanner.nextLine();
            String[] inputs = inputString.split(" ");
            Integer[] integers = Arrays.stream(inputs).map(new Function<String, Integer>() {
                @Override
                public Integer apply(String s) {
                    return Integer.valueOf(s);
                }
            }).collect(Collectors.toList()).toArray(new Integer[]{});
            Arrays.stream(integers).forEach(System.out::println);
        }

        scanner.close();
    }


}
