package com.company.task2;

import javax.swing.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Circle {


    float CentreCoordinateX;
    float CentreCoordinateY;
    float radius;

    static public String symbolNewMess="_)(@";

    public Circle(File file_CoordAndRad) {
            String text=parseStringFromFile(file_CoordAndRad);
            parseCoordAndRadius_fromString(text);

    }

    private void parseCoordAndRadius_fromString(String text) {
        text=text.replaceAll("\r","").trim();
         String[] elements  = text.split("\n");

         try{elements[1]=CommasToDotts( elements[1]).trim();
             this.radius= Float.parseFloat(elements[1]);}catch (Exception p){
             ErrorCritical("radius не спарсился из строки - "+elements[1],p);
         }

         String[] coordXY=  elements[0].trim().split(" ");

        coordXY[0]=CommasToDotts( coordXY[0]).trim();
        coordXY[1]=CommasToDotts( coordXY[1]).trim();
        this.CentreCoordinateX = Float.parseFloat(coordXY[0]);
        this.CentreCoordinateY = Float.parseFloat(coordXY[1]);

    }

    private float[][] parseCoordDotsList(String text) {

        text=text.replaceAll("\r","").trim();
        String[] elements  = text.split("\n");

        String[] coord=null;

        float[][] ArrayResult=new float[elements.length][2];

        for (int i = 0; i < elements.length; i++) {
          coord= elements[i].trim().split(" ");
        try {
            if ( coord.length != 2) ErrorCritical("Не правильно парсятся координаты точек elements ", null);
        }catch (Exception l){
            ErrorCritical("Не правильно парсятся координаты точек elements ", null);
        }


            coord[0]=CommasToDotts( coord[0]).trim();
            coord[1]=CommasToDotts( coord[1]).trim();
            ArrayResult[i][0]= Float.parseFloat(coord[0]);
            ArrayResult[i][1]= Float.parseFloat(coord[1]);
        }

        return ArrayResult;
    }

    public String parseStringFromFile(File file_CoordAndRad){


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file_CoordAndRad));
        } catch (FileNotFoundException e) {
            ErrorCritical("Не удалось прочитать из файла - создать канал ",e);
            e.printStackTrace();
        }

        String output="";
        String st;
        while (true) {
            try {
                if (!((st = br.readLine()) != null)) break;
                output=output+st+System.lineSeparator();
            } catch (IOException e) {
                ErrorCritical("Не удалось прочитать из файла ",e);
                e.printStackTrace();
            }
        }

        return output;

    }

    private void ErrorCritical(String logg, Exception e) {
        if(Main.TurnOffConsoleDebug)return;

        String pattern = "HH:mm:ss MM/dd/ ";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        logg=" "+symbolNewMess+ " " +df.format(today)+"| "+logg;

        System.out.println(logg);

        int desion=  JOptionPane.showConfirmDialog(null,logg+"\n Продолжить выполнение программы?","Ошибка",0);
        if (desion==1) System.exit(1);

    }


    public String[][] checkDotsByCircle(File file_coordDOtS) {
        String textFromFile= this.parseStringFromFile(file_coordDOtS);
        float[][] listCoordDOTS= parseCoordDotsList(textFromFile);
        String [][] listCoordDOTSForPrint=new String[listCoordDOTS.length][3];

        for (int i = 0; i <listCoordDOTS.length ; i++) {
            int DotStatus=-1;
            float LenghtLine= calcLenghtLine_byTwoCoord(listCoordDOTS[i][0],listCoordDOTS[i][1],
                    this.CentreCoordinateX,this.CentreCoordinateY);

            if (LenghtLine==this.radius)DotStatus=0;
            if (LenghtLine>this.radius)DotStatus=2;
            if (LenghtLine<this.radius)DotStatus=1;

            listCoordDOTSForPrint[i][0]= String.valueOf(listCoordDOTS[i][0]);
            listCoordDOTSForPrint[i][1]= String.valueOf(listCoordDOTS[i][1]);
            listCoordDOTSForPrint[i][2]= String.valueOf(DotStatus);

        }
        return listCoordDOTSForPrint;
    }

      public float calcLenghtLine_byTwoCoord(float Dot1X, float Dot1Y,float Dot2X, float Dot2Y) {


        double minus1=  Math.pow ((Dot2X-Dot1X),2);
        double minus2=  Math.pow ((Dot2Y-Dot1Y),2);
        float result= (float) Math.sqrt   (minus1+minus2);

        return result;
    }

    private String CommasToDotts(String input){
       return   input.replaceAll(",",".");
    }
}
