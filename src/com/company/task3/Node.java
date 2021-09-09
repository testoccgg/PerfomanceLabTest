package com.company.task3;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class Node<I, A> {
    public final I id;
    public  A value;
    public ArrayList<Node<I, A>> children = new ArrayList<>();
    public String ParentIDnode;

    public void setJSONid(String JSONid) {
        this.JSONid = JSONid;
    }

    public void setJSONvalue(String JSONvalue) {
        this.JSONvalue = JSONvalue;
    }

    public String JSONid;
    public String JSONvalue;

    public ArrayList<AttrEntryClass> getJSONattributes() {
        return JSONattributes;
    }

    public void addJSONattributes(String typeAttr,String val) {
        AttrEntryClass attrEntryClass=new AttrEntryClass(typeAttr,val);
        this.JSONattributes.add(attrEntryClass);
    }

    public ArrayList<AttrEntryClass> JSONattributes=new ArrayList<>();

    public String getJson() {
        return Json;
    }

    private String Json;

    public void setJson(String json) {
        Json = json;
    }



    public Node(I id, A value,String ParentID) {
        this.id = id;
        this.value = value;
        this.ParentIDnode=ParentID;
    }




    class AttrEntryClass{

        String type;
        String value;

        public AttrEntryClass(String type, String value) {
            this.type = type;
            this.value = value;
        }
    }
}
