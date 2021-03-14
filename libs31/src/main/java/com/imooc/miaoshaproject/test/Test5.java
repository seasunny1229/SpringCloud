package com.imooc.miaoshaproject.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Test5 {


    public static void main(String... args){

        Scanner scanner = new Scanner(System.in);

        String numberString = scanner.nextLine();
        String idInputString = scanner.nextLine();
        String scoreInputString = scanner.nextLine();

        HashMap<Integer, List<Integer>> scoreMap = new HashMap<>();

        String[] idStrings = idInputString.split(",");
        String[] scoreStrings = scoreInputString.split(",");

        for(int i=0;i<idStrings.length;i++){
            int id = Integer.valueOf(idStrings[i]);
            int score = Integer.valueOf(scoreStrings[i]);
            if(!scoreMap.containsKey(id)){
                scoreMap.put(id, new ArrayList<>());
            }
            scoreMap.get(id).add(score);
        }


        TreeMap<Integer, TreeSet<Integer>> treeMap = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return t1 - integer;
            }
        });


        for(Map.Entry<Integer, List<Integer>> entry : scoreMap.entrySet()){
            int id = entry.getKey();
            List<Integer> scores = entry.getValue();
            if(scores.size() < 3){
                continue;
            }

            Collections.sort(scores);
            int sum = 0;
            for(int i=0;i<3;i++){
                sum += scores.get(scores.size() - i - 1);
            }

            if(!treeMap.containsKey(sum)){

                TreeSet<Integer> treeSet = new TreeSet<>(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer integer, Integer t1) {
                        return t1 - integer;
                    }
                });

                treeMap.put(sum, treeSet);
            }

            treeMap.get(sum).add(id);
        }

        StringBuilder sb = new StringBuilder();

        for(Map.Entry<Integer, TreeSet<Integer>> entry : treeMap.entrySet()){
            TreeSet<Integer> treeSet = entry.getValue();
            for(Integer integer : treeSet){
                sb.append(integer);
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.length()-1);

        System.out.println(sb.toString());

        scanner.close();

    }



}
