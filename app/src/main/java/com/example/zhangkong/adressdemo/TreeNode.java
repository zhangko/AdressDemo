package com.example.zhangkong.adressdemo;

/**
 * Created by ZhangKong on 2015/5/21.
 */
public class TreeNode {
    private String Name;
    private String DepartmenID;
    private String ID;
    private String Code;
    private int level;
    private boolean haveParent;
    private boolean haveChild;
    private String parent;
    private boolean expanded;
    public TreeNode(){


    }
    public void setName(String name){
        this.Name = name;
    }
    public void setDepartmenID(String departmentid){
        this.DepartmenID = departmentid;
    }
    public void setID(String id){
        this.ID = id;
    }
    public void setLevel(int level){
        this.level = level;
    }
    public void setCode(String code){ this.Code = code ;}
    public String getCode(){return this.Code;}
    public void setHaveParent(boolean flag){
        this.haveParent = flag;
    }
    public void setHaveChild(boolean flag){
        this.haveChild = flag;
    }
    public void setParent(String parent){
        this.parent = parent;
    }
    public String getName(){
        return this.Name;
    }
    public String getDepartmenID(){
        return this.DepartmenID;
    }
    public String getID(){
        return this.ID;
    }
    public String getParent(){
        return this.parent;
    }
    public int getLevel(){
        return this.level;
    }
    public boolean isExpanded() {
        return expanded;
    }
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    public boolean ishaveParent(){return haveParent;}
    public boolean isHaveChild(){return haveChild;}

}
