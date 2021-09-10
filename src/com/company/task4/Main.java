package com.company.task4;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    static public String symbolNewMess="_)(@";
    static boolean TurnOffConsoleDebug=true;

    public static void main(String[] args) {


        String PathArrNums=args[0];

        int[] numsArray=readFromFile(PathArrNums);

        Arrays.sort(numsArray);

        int Mediana=0;
        if (numsArray.length%2==0){
          int firstNum= numsArray[  numsArray.length/2 -1];
            int secondNum= numsArray [  numsArray.length/2 ];
            Mediana=(firstNum+secondNum)/2;
        }else {
            Mediana=numsArray[numsArray.length/2];
        }

        int result=0;
        for (int i : numsArray) {
            int minus=i-Mediana;
            minus=Math.abs(minus);
            result=result+minus;

        }

        if (result<2)System.out.println("Количество Ходов меньше 2!");

        System.out.println(result);

    }

    private static int[] readFromFile(String pathArrNums) {

        List<Integer> arrayList=new ArrayList<Integer>();

        try (BufferedReader br = new BufferedReader(new FileReader(pathArrNums))) {
            String line;
            while ((line = br.readLine()) != null) {
                int parsed= Integer.parseInt(line);
                arrayList.add(parsed);
            }
        }catch (Exception p){
            ErrorCritical("",p);
        }
        int[] result = arrayList.stream().mapToInt(i -> i).toArray();

        return result;
    }

    private static void ErrorCritical(String logg, Exception e) {
        if(TurnOffConsoleDebug)return;

        String pattern = "HH:mm:ss MM/dd/ ";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        logg=" "+symbolNewMess+ " " +df.format(today)+"| "+logg;

        System.out.println(logg);

        e.printStackTrace();

        int desion=  JOptionPane.showConfirmDialog(null,logg+"\n Продолжить выполнение программы?","Ошибка",0);
        if (desion==1) System.exit(1);

    }

}
