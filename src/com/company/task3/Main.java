package com.company.task3;

import netscape.javascript.JSObject;

import javax.swing.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {

    public static boolean TurnOffConsoleDebug=true;


    public static void main(String[] args) {

        // реализация без библиотеки json парсинга values , работа с шаблоном уже с помощью simple json

//        String PatternReportJSONPath="src/com/company/task3/tests.json";
//        String valuesJSONPath="src/com/company/task3/values.json";

        String PatternReportJSONPath=args[0];
        String valuesJSONPath=args[1];

        String ValuesJSON= ReporterJson.parseStringFromFileStatic(new File(valuesJSONPath));

        ReporterJson reporterJson=new ReporterJson(PatternReportJSONPath);
        reporterJson.loadValues(ValuesJSON);

        String JSONreport= reporterJson.generateJSONReport();

        String newPath=new File(args[0]).getAbsolutePath();
        newPath=newPath.replaceAll(new File(args[0]).getName(),"");
        newPath=newPath+"report.json";
        reporterJson.writeToFile(JSONreport,newPath);








    }



}
