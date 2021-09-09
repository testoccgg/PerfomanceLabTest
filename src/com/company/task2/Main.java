package com.company.task2;

import java.io.File;

public class Main {

    public static boolean TurnOffConsoleDebug=true;

    public static void main(String[] args) {
	// write your code here


//        File file_CoordAndRad=new File("src/com/company/task2/file1");
//        File file_CoordDOtS=new File("src/com/company/task2/file2");

        File file_CoordAndRad=new File(args[0]);
        File file_CoordDOtS=new File(args[1]);


        Circle circle=new Circle(file_CoordAndRad);

        String[][] results=  circle.checkDotsByCircle(file_CoordDOtS);

        for (String[] result : results) {
            System.out.println(result[2]);
        }


    }
}
