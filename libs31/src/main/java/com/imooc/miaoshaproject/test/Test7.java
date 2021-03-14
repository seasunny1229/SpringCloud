package com.imooc.miaoshaproject.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 *
 * 3
 * 3
 * 8 35
 * 5 10
 * 1 3
 *
 */

public class Test7 {

    public static void main(String... args){

        Scanner scanner = new Scanner(System.in);

        int numInterviewers = 0;
        int numCurr = 0;
        int currFinishTime = 0;

        String numInterviewInput = scanner.nextLine();
        int numberInterview = Integer.valueOf(numInterviewInput);

        String numInput = scanner.nextLine();
        int number = Integer.valueOf(numInput);

        List<Integer[]> times = new ArrayList<>();
        for(int i=0;i<number;i++){
            String[] strings = scanner.nextLine().split(" ");
            times.add(new Integer[]{Integer.valueOf(strings[0]), Integer.valueOf(strings[1])});
        }

        Collections.sort(times, new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] integers, Integer[] t1) {
                return integers[0] - t1[0];
            }
        });



        while (times.size() > 0){

            if(numCurr == 0) {
                numInterviewers++;
            }

            if(numCurr == 0){
                Integer[] time = times.remove(0);
                currFinishTime = time[1];
                numCurr++;
            }
            else {

                Integer[] select= null;
                for(int i=0;i<times.size();i++){
                    if(times.get(i)[0] >= currFinishTime){
                        select = times.get(i);
                        break;
                    }
                }

                if(select != null){
                    numCurr++;
                    times.remove(select);
                    currFinishTime = select[1];
                    Collections.sort(times, new Comparator<Integer[]>() {
                        @Override
                        public int compare(Integer[] integers, Integer[] t1) {
                            return integers[0] - t1[0];
                        }
                    });

                    if(numCurr > numberInterview){
                        //numInterviewers ++;
                        numCurr = 0;
                        currFinishTime = 0;
                    }
                }
                else {
                    //numInterviewers ++;
                    numCurr = 0;
                    currFinishTime = 0;
                }

            }




        }


        System.out.println(numInterviewers);

        scanner.close();
    }



}
