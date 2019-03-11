package com.buggyani.random;

import java.util.Random;

class common_test {

    public static void main(String[] args) {
        get_random_for(100000000);
//        get_random_while(100000000);
    }


    public static int get_zero_or_one() {
        Random randomGenerator = new Random();
        int randomInteger = randomGenerator.nextInt(2);
        return randomInteger;
    }

    public static int get_random_for(int max) {
        long start = System.currentTimeMillis();
        int random = 0;

//        for (int i = 0; i < max; i++) {
//        for (int i = max; --i >= 0; ) {
//        for (int i = max; i > 1; i--) {
        for (int i = max - 1; i >= 0; i--) {
            random += get_zero_or_one();
        }
        long end = System.currentTimeMillis();
        System.out.println("for time = " + (end - start) / 1000.0);
        System.out.println("random = " + random);
        return random;
    }

    public static int get_random_while(int max) {
        long start = System.currentTimeMillis();
        int random = 0;
        int i = 0;
        while (i < max) {
            random += get_zero_or_one();
            i++;
        }
        long end = System.currentTimeMillis();
        System.out.println("while time = " + (end - start) / 1000.0);
        System.out.println("random = " + random);
        return random;
    }


}