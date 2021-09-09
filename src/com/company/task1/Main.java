package com.company.task1;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static boolean TurnOffConsoleDebug=true;
    static public String symbolNewMess="_)(@";

    public static void main(String[] args) {


        int length=0;
        int length_Interval=0;

        try {
             length= Integer.parseInt(args[0]);
             length_Interval= Integer.parseInt(args[1]);
        }catch (Exception p){
            p.printStackTrace();
            System.exit(1);
        }




        int[] input=new int[length];
        for (int i = 0; i < input.length; i++) {
            input[i]=i+1;
        }

        boolean EndIsFirst=false;


        CounterByArray counterByArray=new CounterByArray(length);
        CounterRunByInterval counterByInterval=new CounterRunByInterval(length_Interval);
        FindRepeatChain findRepeatChain=new FindRepeatChain();


        int FirstDigit=input[0];
        int LastDigit=-1;
        String CurrentIntervalString="";
        String LastIntervalString="";

                // проверить цепочки еще можно
        while ( !findRepeatChain.isChainWasIdent()){

            int currentDigit= input[counterByArray.getIteratorVal()];
            counterByInterval.PrintLN_useColors(currentDigit);






            if (counterByInterval.isItIntervalcheck()){
                CurrentIntervalString=CurrentIntervalString+currentDigit;
            }else {
                            if (CurrentIntervalString.length()>0){
                                LastIntervalString=CurrentIntervalString;
                                CurrentIntervalString="";
                            }
              }

            if (counterByInterval.checkItsFirstDIG()){
                FirstDigit=currentDigit;


                    if (LastDigit==FirstDigit){
                        counterByInterval.addCountStepsInterval();
                        findRepeatChain.addInterval(LastIntervalString);
                    }else {
                        if (!counterByInterval.isItFirstIterate()){
                            ErrorCritical("Цепочка интервалов разорвалась",null);
                        }

                    }


            }



            if (counterByInterval.checkItsLastDIG()){
                LastDigit=currentDigit;
            }






            counterByInterval.goONE();
            counterByArray.goOne();

        }

        // результат
        System.out.println(findRepeatChain.wayResult);


    }

    public static void ErrorCritical(String logg, Exception e) {
        if(TurnOffConsoleDebug)return;

        String pattern = "HH:mm:ss MM/dd/ ";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        logg=" "+symbolNewMess+ " " +df.format(today)+"| "+logg;

        System.out.println(logg);

        int desion=  JOptionPane.showConfirmDialog(null,logg+"\n Продолжить выполнение программы?","Ошибка",0);
        if (desion==1) System.exit(1);

    }


    static class FindRepeatChain{

        Map<String, Integer> uniqueList = new HashMap<String, Integer>();
        ArrayList<String> chain=new ArrayList<>();

        String wayResult="";

        public boolean isChainWasIdent() {
            return chainWasIdent;
        }

        private boolean chainWasIdent=false;

        FindRepeatChain(){

        }

        void  addInterval(String interval){
            if (checkUnique(interval)){
                chain.add(interval);
                return;
            }else {
                chainWasIdent=true;
                for (int i = 0; i <chain.size() ; i++) {
                    wayResult=wayResult+ chain.get(i).charAt(0);
                }


            }

        }

        boolean checkUnique(String interval){

            if (!uniqueList.containsKey(interval)) {
                uniqueList.put(interval,0);
                return true;
            }


            return false;
        }

    }

    static class CounterRunByInterval{

        private int SizeInterval;
        private int currentIterate =0;

        public boolean isItIntervalcheck() {
            return IsItInterval;
        }

        private boolean IsItInterval=true;

        public boolean isItFirstIterate() {
            return firstIterate;
        }

        private boolean firstIterate=true;

        public int getCountStepsInterval() {
            return CountStepsInterval;
        }

        public void addCountStepsInterval() {
            CountStepsInterval=CountStepsInterval+1;
        }

        int CountStepsInterval=0;


        CounterRunByInterval(int SizeInterval){
            this.SizeInterval=SizeInterval;
        }

        public int goONE(){
            currentIterate++;
            if (currentIterate >=SizeInterval)
            {   currentIterate =0;
                IsItInterval=!IsItInterval;
            }

            return currentIterate;

        }
        int getCurrentIterate(){
//            if (currentIterate==0 && !checkItFirstInterval() ){
//
//            }
            return currentIterate;
        }

        public void PrintLNuseColors(String text){
            if (TurnOffConsoleDebug)return;
            if (IsItInterval) {

                System.out.println(ConsoleColors.GREEN+text);
            } else {
                System.out.println(ConsoleColors.RESET+text);
            }
        }

        public void PrintLN_useColors(int integer){
            if(TurnOffConsoleDebug)return;

            if (IsItInterval) {

                System.out.print(ConsoleColors.GREEN+integer);
                System.out.print(ConsoleColors.RESET+"");
            } else {
                System.out.print(ConsoleColors.RESET+integer);
            }
        }

        public boolean checkItsFirstDIG(){

            if (currentIterate==0 && IsItInterval) return true;
            return false;
        }
        public boolean checkItsLastDIG(){
            if (currentIterate==SizeInterval-1 && IsItInterval){
                firstIterate=false;
                return true;
            }
            return false;
        }

//        boolean checkItFirstInterval(){
//           try {
//               if (LastDigit==0) {new String();}
//               return false;
//           }catch (Exception p){
//               return true;
//           }
//        }


    }

    static class CounterByArray{
        private int IteratorVal=0;
        private int SizeOfArray;


        CounterByArray(int SizeOfArray){
           this.SizeOfArray=SizeOfArray;
        }

        int getIteratorVal(){
            return IteratorVal;
        }

        void goOne(){
            IteratorVal++;
            if (IteratorVal>=SizeOfArray){
                IteratorVal=0;
            }
        }
    }



}
