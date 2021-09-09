package com.company.task3;

//import com.company.task3.JSONlib.JSONArray;
//import com.company.task3.JSONlib.JSONObject;
//import com.company.task3.JSONlib.parser.JSONParser;
//import com.company.task3.JSONlib.parser.ParseException;
import netscape.javascript.JSObject;

import javax.swing.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReporterJson {

    static public String symbolNewMess="_)(@";
    String PathReportPath;

    ArrayList<Node> valuesNodes;

    public ReporterJson(String pathReportPath) {
        PathReportPath = pathReportPath;
    }






    public void  loadValues(String PatternReportJSON){
        ArrayList<Node> nodesList =  ParseDOMStructure(PatternReportJSON);
        valuesNodes=nodesList;

    }


    private ArrayList<Node>  ParseDOMStructure(String patternReportJSON) {

        patternReportJSON=patternReportJSON.replaceAll("\r","");
        patternReportJSON=patternReportJSON.replaceAll("\n","");

        String taggedNodes= tagsNodes(patternReportJSON);
        ArrayList<Node> nodesList =generateNodesList(taggedNodes);
         nodesList =deleteNodesDuplicates(nodesList);
        nodesList =deleteNodesTAGs(nodesList);
        nodesList =parseAttrsjson(nodesList);
        nodesList =setStandartAttrs(nodesList);

        return nodesList;
        //надо бы еще дерево сделать со связями
    }

    private ArrayList<Node> setStandartAttrs(ArrayList<Node> nodesList) {

        for (int i = 0; i < nodesList.size(); i++) {
            for (int b = 0; b < nodesList.get(i).JSONattributes.size(); b++) {
                Node.AttrEntryClass attrEntryClass= (Node.AttrEntryClass) nodesList.get(i).JSONattributes.get(b);
                  String type=attrEntryClass.type;
                  String val=attrEntryClass.value;
                  if (type.equals("id")){
                      nodesList.get(i).JSONid=val;
                      nodesList.get(i).JSONattributes.remove(b);
                      b=-1;
                  }
                if (type.equals("value")){
                    nodesList.get(i).JSONvalue=val;
                    nodesList.get(i).JSONattributes.remove(b);
                    b=-1;
                }

            }
        }

        for (Node node : nodesList) {
            if ( node.JSONattributes.size()>0)ErrorCritical("Еще атрибуты не расписаны в объектте",null);
        }

        return nodesList;
    }

    private ArrayList<Node> parseAttrsjson(ArrayList<Node> nodesList) {

        for (Node node : nodesList) {

            String jsonCODE= node.getJson();
            String jsonCODEBackup= node.getJson();

            Matcher matcherStrings= Pattern.compile("\"[^,\\{\\}]+\":\"(.*)\"").matcher(jsonCODE);
            while (matcherStrings.find()) {
                String parse = matcherStrings.group();
                jsonCODE=jsonCODE.replaceAll(parse,"");
                parse = parse.replaceAll("\"", "");
                String[] chainTWO = parse.split(":");
                node.addJSONattributes(chainTWO[0], chainTWO[1]);
            }


                Matcher matcherInt= Pattern.compile("\"[^,\\{\\}]+\":\\d+").matcher(jsonCODE);
                while (matcherInt.find()){
                    String parse = matcherInt.group();
                    jsonCODE=jsonCODE.replaceAll(parse,"");
                    parse = parse.replaceAll("\"", "");
                    String[] chainTWO = parse.split(":");
                    node.addJSONattributes(chainTWO[0], chainTWO[1]);
            }

                jsonCODE=jsonCODE.replaceAll("[,{}]","");
                if (jsonCODE.length()>0){
                    if (jsonCODE.indexOf("[]")==-1) ErrorCritical("Еще атрибуты json не пропарсились",null);
                }

        }

        return nodesList;
    }

    private ArrayList<Node> deleteNodesTAGs(ArrayList<Node> nodesList) {


        for (int i = nodesList.size() - 1; i >= 0; i--) {
           String editJSON=nodesList.get(i).getJson();
           editJSON=editJSON.replaceAll("TAGG(\\d+)\\|\\|tagto","");
            nodesList.get(i).setJson(editJSON);
        }

        return nodesList;
    }

    private ArrayList<Node> deleteNodesDuplicates(ArrayList<Node> nodesList) {

        Map<String, Node> hashMap = new HashMap<String, Node>();

        for (int i = 0; i < nodesList.size(); i++) {
            String key= (String) nodesList.get(i).id;
            if (hashMap.containsKey(key)){
                if (!hashMap.get(key).getJson().
                        equals(nodesList.get(i).getJson()))ErrorCritical("Неправильно зачищаются дубликаты в списке узлов", null);
            }else {
                hashMap.put(key,nodesList.get(i));
            }
        }

        if (hashMap.size()!=nodesList.size()){
            ErrorCritical("Создать запись из хэшмап в лист  - deleteNodesDuplicates()", null);
        }


        return nodesList;

    }

    private  ArrayList<Node> generateNodesList(String taggedNodes) {
        String regexCurlyBraces = "\\{TAG[^\\}\\{]*\\}";

        taggedNodes = taggedNodes.replaceAll("\\s+", "");

        String taggedNodesBackup = taggedNodes;

        Pattern pattern = Pattern.compile(regexCurlyBraces, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(taggedNodes);

        ArrayList<Node> nodesList = new ArrayList<>();


        while (taggedNodes.indexOf("TAG") > -1) {



        while (matcher.find()) {

            String nodeString = matcher.group();


            String checkTag = nodeString.replaceFirst("TAGG", "");
            if (checkTag.indexOf("TAGG") > -1) {
                ErrorCritical("Проблемы при вормирование NODEs", null);
            }

            Matcher matcher1 = Pattern.compile("\\{TAGG(\\d+)\\|\\|", Pattern.MULTILINE).matcher(nodeString);
            String id = "";
            while (matcher1.find()) {
                id = matcher1.group(0);
                id = id.replaceAll("\\D", "");
                break;
            }

            if (id.length()==0){
                ErrorCritical("id.length()==0",null);
            }
//            String id=nodeString.replaceAll("\\{TAGG(\\d)\\|\\|","")
            Node node = new Node(id, null, null);
            node.setJson(nodeString);
            nodesList.add(node);
        }

          taggedNodes = taggedNodes.replaceAll("\\{TAG[^\\}\\{]*\\}", "");

             pattern = Pattern.compile(regexCurlyBraces, Pattern.MULTILINE);
             matcher = pattern.matcher(taggedNodes);
            matcher.reset();

        }

        for (Node node : nodesList) {
            PrintInfo(String.valueOf(node.id));
        }




        return  nodesList;
    }

    private void createTree(String taggedNodes) {

    }

    private String tagsNodes(String patternReportJSON) {

         String regexCurlyBraces = "\\{[^\\}\\{]*\\}";

        String patternReportJSONBackup=patternReportJSON;

        String  tagedString=patternReportJSON;
        String  tagStringClear=patternReportJSON;

        int iterator=0;
        String TagFrom="TAGG";
        String TagTo="||tagto ";

        tagStringClear=tagStringClear.replaceAll("\\{"," { ");
        tagStringClear=tagStringClear.replaceAll("\\}"," } ");


        int itHave=   tagStringClear.indexOf(" { ");
        while (itHave!=-1){

            tagStringClear= tagStringClear.replaceFirst(" \\{ ","{"+TagFrom+iterator+TagTo);
            iterator++;
            itHave=   tagStringClear.indexOf(" { ");
        }


        return tagStringClear;
        /*
//         tagStringClear=tagStringClear.replaceAll("\\{"," {");
//        tagStringClear=tagStringClear.replaceAll("\\}","} ");

        tagString=tagString.replaceAll("\\{"," {");
        tagString=tagString.replaceAll("\\}","} ");

        tagString=tagString.replaceAll("\\s+","");
        tagStringClear=tagStringClear.replaceAll("\\s+","");

         Pattern pattern = Pattern.compile(regexCurlyBraces, Pattern.MULTILINE);
         Matcher matcher = pattern.matcher(tagStringClear);

         boolean breakCircle=false;


         while (!breakCircle){
             while (matcher.find()) {

                 String delete= matcher.group(0);
            try {
                tagStringClear=tagStringClear.replace(delete,"");
            }catch (Exception ppp){
               println(1);
            }

                 String deleteTagged= delete.replace("{","{"+TagFrom+iterator+TagTo);
                  deleteTagged= deleteTagged.replaceAll("\\{","").replaceAll("\\}","");
                 deleteTagged=deleteTagged.replaceAll("\\s+","").trim();

//                  if (  tagString.indexOf(delete.replaceAll("\\{","").replaceAll("\\}",""))==-1){
//                    println(1);
//                  }

                  tagString=tagString.replaceAll("\\s+","");
                 delete=delete.replaceAll("\\{","").replaceAll("\\}","").trim();
                 tagString=tagString.replace(delete,deleteTagged);
                 iterator++;

             }
             if (tagStringClear.indexOf("{")==-1){
                 breakCircle=true;
             }

             pattern = Pattern.compile(regexCurlyBraces,Pattern.MULTILINE);
              matcher = pattern.matcher(tagStringClear);
              matcher.reset();

         }

         tagStringClear=tagString;
        tagStringClear= tagStringClear.replaceAll("\\{[^\\}\\{]*\\}","");
*/


//        while ( Pattern.compile(regexCurlyBraces).matcher(tagStringClear).find()) {
//            String delete="";
//            try{
//                delete= Pattern.compile(regexCurlyBraces).matcher(tagStringClear).group();
//            }catch (Exception pp){
//
//            }
//                 tagStringClear=tagStringClear.replace(delete,"");
//                 String deleteTagged= delete.replace("{","{"+TagFrom+iterator+TagTo);
//                 tagString=tagString.replace(delete,deleteTagged);
//                 iterator++;
//
//             }



    }

    public static String parseStringFromFileStatic(File file_CoordAndRad){


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



    public  String parseStringFromFile(File file_CoordAndRad){


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


    static void PrintInfo(String logg){
        if(Main.TurnOffConsoleDebug)return;
        System.out.println(logg);
    }

    private static void ErrorCritical(String logg, Exception e) {
        if(Main.TurnOffConsoleDebug)return;

        String pattern = "HH:mm:ss MM/dd/ ";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        logg=" "+symbolNewMess+ " " +df.format(today)+"| "+logg;

        System.out.println(logg);

        e.printStackTrace();

        int desion=  JOptionPane.showConfirmDialog(null,logg+"\n Продолжить выполнение программы?","Ошибка",0);
        if (desion==1) System.exit(1);

    }


    public void showReport() {

        String json=  this.parseStringFromFile(new File(this.PathReportPath));

        String jsonBackup=json;
        json=json.replaceAll("[\\s\n]","");

        Matcher matcher= Pattern.compile("(?=\"id)(.*?)(?>\"value\":\"\")").matcher(json);
        ArrayList<String> toEdit=new ArrayList();
        while (matcher.find()){
            String line=matcher.group();
            toEdit.add(line);
        }

        for (Node valuesNode : this.valuesNodes) {
            String findID= valuesNode.JSONid;
            for (String editLine : toEdit) {
                if (editLine.indexOf("id\":"+findID)>-1){
                    String newLine=editLine.replaceAll("value\":\"","value\":\""+valuesNode.JSONvalue);
                try {
                    editLine=editLine.replaceAll("\"\"","\"");
                    json=json.replaceAll(editLine,newLine);
                }catch (Exception pp){
                   ErrorCritical("ошибка",pp);

                }


                }
            }
        }

    }
    /*
    public void proval() {

        JSONParser parser = new JSONParser();

       String json=  this.parseStringFromFile(new File(this.PathReportPath));
        JSONObject jsonReportPattern=new JSONObject();
       try {
             jsonReportPattern = (JSONObject) parser.parse(json);
        } catch (ParseException e) {
            ErrorCritical("Не прочелся json",e);
        }


        for(Iterator iterator = jsonReportPattern.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
           println(jsonReportPattern.get(key));
        }


    }

     */
}
