package com.imooc.miaoshaproject.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

public class Test4 {

    // 最后的总和的最小值
    private static Integer sum = Integer.MAX_VALUE;

    public static void main(String... args){

        // 输入数据
        List<Integer[]> inputs = new ArrayList<>();
        inputs.add(new Integer[]{2});
        inputs.add(new Integer[]{3,4});
        inputs.add(new Integer[]{6,5,7});
        inputs.add(new Integer[]{4,1,8,3});

        // 第几层
        int step = 0;

        // 中间每一层选定的数据
        Integer[] tmp = new Integer[inputs.size()];

        // 递归及回溯法调用
        calculate(inputs, step, tmp);

        // 输入结果
        System.out.println("The result is : " + sum);

    }

    private static void calculate(List<Integer[]> inputs, int step, Integer[] tmp){

        // 判断是否到最后一步
        if(step >= inputs.size()){

            // 求和
            Integer sumTmp = Arrays.stream(tmp).reduce(new BinaryOperator<Integer>() {
                @Override
                public Integer apply(Integer integer, Integer integer2) {
                    return integer + integer2;
                }
            }).get();

            // 判断新的是否小于原来的，如果是，替换原来的
            if(sumTmp < sum){
                sum = sumTmp;
            }
        }

        else {

            // 遍历当下数组
            for(Integer integer : inputs.get(step)){
                tmp[step] = integer;
                calculate(inputs, step+1, tmp);
            }

        }

    }




}
