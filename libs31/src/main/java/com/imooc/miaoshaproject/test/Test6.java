package com.imooc.miaoshaproject.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;


public class Test6 {

    public static void main(String... args){

        Scanner scanner = new Scanner(System.in);

        boolean isUnvalid = false;

        // input
        String firstLine = scanner.nextLine();

        // number judges and candidates
        String[] firstLineSplit = firstLine.split(",");
        int numJudge = Integer.valueOf(firstLineSplit[0]);
        int numCandidate = Integer.valueOf(firstLineSplit[1]);

        // each score
        // id, score->number
        HashMap<Integer, TreeMap<Integer, Integer>> hashMap = new HashMap<>();

        // sum score
        // id, sum score
        HashMap<Integer, Integer> sumMap = new HashMap<>();

        for(int i=0;i<numJudge;i++){
            String line = scanner.nextLine();
            String[] split = line.split(",");
            for(int j=0;j<split.length;j++){

                // score
                String string = split[j];
                int score = Integer.valueOf(string);

                // id
                int id = j+1;

                if(score > 10 || score < 1){
                    isUnvalid = true;
                }
                else {

                    // put score in tree map
                    if(!hashMap.containsKey(id)){
                        TreeMap<Integer, Integer> treeMap = new TreeMap<>(new Comparator<Integer>() {
                            @Override
                            public int compare(Integer integer, Integer t1) {
                                return t1 - integer;
                            }
                        });
                        hashMap.put(id, treeMap);
                    }
                    TreeMap<Integer, Integer> treeMap = hashMap.get(id);
                    if(!treeMap.containsKey(score)){
                        treeMap.put(score, 1);
                    }
                    else {
                        treeMap.put(score, treeMap.get(score) + 1);
                    }

                    // add score in sum map
                    if(!sumMap.containsKey(id)){
                        sumMap.put(id, score);
                    }
                    else {
                        sumMap.put(id, sumMap.get(id) + score);
                    }
                }
            }
        }

        if(numJudge < 3 || numCandidate < 3 || numJudge > 10 || numCandidate > 100){
            isUnvalid = true;
        }


        if(!isUnvalid){

            // sum score -> id tree set
            TreeMap<Integer, TreeSet<Integer>> treeMap = new TreeMap<>(new Comparator<Integer>() {
                @Override
                public int compare(Integer integer, Integer t1) {
                    return t1 - integer;
                }
            });

            for(Map.Entry<Integer, Integer> entry : sumMap.entrySet()){
                int id = entry.getKey();
                int score = entry.getValue();

                if(!treeMap.containsKey(score)){
                    TreeSet<Integer> treeSet = new TreeSet<>(new Comparator<Integer>() {
                        @Override
                        public int compare(Integer integer, Integer t1) {
                            return Test6.compare(hashMap.get(t1), hashMap.get(integer));
                        }
                    });

                    treeMap.put(score, treeSet);
                }
                treeMap.get(score).add(id);
            }


            List<Integer> ids = new ArrayList<>();
            for(Map.Entry<Integer, TreeSet<Integer>> entry : treeMap.entrySet()){
                TreeSet<Integer> integers = entry.getValue();
                for(Integer integer : integers){
                    ids.add(integer);
                }
            }

            StringBuilder sb = new StringBuilder();
            for(int i=0;i<3;i++){
                sb.append(ids.get(i));
                sb.append(",");
            }
            sb.deleteCharAt(sb.length()-1);
            System.out.println(sb.toString());
        }
        else {
            System.out.println(-1);
        }


        scanner.close();
    }

    private static int compare(TreeMap<Integer, Integer> value1, TreeMap<Integer, Integer> value2){
        for(int i=10;i>0;i--){
            if(value1.containsKey(i) && !value2.containsKey(i)){
                return 1;
            }

            else if(!value1.containsKey(i) && value2.containsKey(i)){
                return -1;
            }

            else if(value1.containsKey(i) && value2.containsKey(i) && value1.get(i) - value2.get(i) != 0){
                return value1.get(i) - value2.get(i);
            }
        }

        return 0;
    }


}
