package com.sakibuddinbhuiyan.zikir.utils;

import com.sakibuddinbhuiyan.zikir.models.Zikir;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class PublicVariables {
    public static LinkedList<Zikir> zikirObjList = new LinkedList<Zikir>();
    public static LinkedList<String> zikrList = new LinkedList<>();
    public static LinkedList<String> zikrBanglaList = new LinkedList<>();
    public static LinkedList<Integer> todayList = new LinkedList<>();
    public static LinkedList<Integer> totalList = new LinkedList<>();
    public static LinkedList<Integer> favouriteList = new LinkedList<>();
    public static LinkedList<String> zikirLanguages = new LinkedList<>(Arrays.asList("English","Bangla"));
    public static String selectedLanguage;
    public static int vibrate;
    public static HashMap<Character,Character> numberMap = new HashMap<>();
    static {
        numberMap.put('0','০');
        numberMap.put('1','১');
        numberMap.put('2','২');
        numberMap.put('3','৩');
        numberMap.put('4','৪');
        numberMap.put('5','৫');
        numberMap.put('6','৬');
        numberMap.put('7','৭');
        numberMap.put('8','৮');
        numberMap.put('9','৯');
    }
}
