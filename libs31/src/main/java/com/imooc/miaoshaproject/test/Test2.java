package com.imooc.miaoshaproject.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.function.IntConsumer;
import java.util.function.ToIntFunction;

public class Test2 {

    public static void main(String... args){

        Scanner scanner = new Scanner(System.in);
        HashMap<Integer, Integer> map = new HashMap<>();

        String input = scanner.nextLine();
        String[] inputs = input.split(",");
        Arrays.stream(inputs).mapToInt(new ToIntFunction<String>() {
            @Override
            public int applyAsInt(String s) {
                return Integer.valueOf(s);
            }
        }).forEach(new IntConsumer() {
            @Override
            public void accept(int i) {
                if(!map.containsKey(i)){
                    map.put(i,0);
                }
                int count = map.get(i);
                map.put(i, count+1);
            }
        });

        int numMapKeys = map.entrySet().size();
        Set<Integer> set = new HashSet<>(map.values());

        System.out.println(numMapKeys == set.size()? "true" :"false");

        scanner.close();

    }


}
