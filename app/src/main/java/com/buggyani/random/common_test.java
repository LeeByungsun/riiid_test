package com.buggyani.random;

import java.util.Random;

class common_test {
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        // System.out.println("time = " + start);
        int random = get_random(100000000);
        long end = System.currentTimeMillis();
        // System.out.println("time = " + end);
        System.out.println("time = " + (end - start) / 1000.0);
        System.out.println("random = " + random);
    }

    public static int get_zero_or_one() {
        Random randomGenerator = new Random();
        int randomInteger = randomGenerator.nextInt(2);
        return randomInteger;
    }

    public static int get_random(int max) {
        int num = 0;
        for (int i = 0; i < max; i++) {
            num += get_zero_or_one();
        }
        return num;
    }

}