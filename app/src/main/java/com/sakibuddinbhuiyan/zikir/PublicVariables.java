package com.sakibuddinbhuiyan.zikir;

import com.sakibuddinbhuiyan.zikir.database.Zikir;

import java.util.Arrays;
import java.util.LinkedList;

public class PublicVariables {
    public static LinkedList<Zikir> zikirObjList = new LinkedList<Zikir>();
    public static LinkedList<String> zikrList = new LinkedList<>();
    public static LinkedList<Integer> todayList = new LinkedList<>();
    public static LinkedList<Integer> totalList = new LinkedList<>();
    public static LinkedList<Integer> favouriteList = new LinkedList<>();
    public static LinkedList<String> zikirLanguages = new LinkedList<>(Arrays.asList("English","Bangla"));
    public static String selectedLanguage;
    public static int vibrate;
}
