package com.example.zhangkong.adressdemo;

/**
 * Created by ZhangKong on 2015/5/12.
 */
public class Person {
    public String name;
    public String worknumber;
    public String workId;
    public String workDepartmentID;
    public String workDepartmentCode;
    public String workmobile;
    public String worktelephone;
    public String workmail;
    public int sex;
    public int ismaindepartment;
    public Person(){

    }

    public String getName(){
        return this.name;
    }
    public String getWorknumber(){
        return this.worknumber;
    }
    public String getWorkId(){
        return this.workId;
    }
    public String getWorkDepartmentID(){
        return this.workDepartmentID;
    }
    public String getWorkDepartmentCode(){
        return this.workDepartmentCode;
    }
    public String getWorkmobile(){
        return this.workmobile;
    }
    public String getWorktelephone(){
        return this.worktelephone;
    }
    public String getWorkmail(){
        return this.workmail;
    }
    public int getSex(){
        return this.sex;
    }
    public int getIsmaindepartment(){
        return this.ismaindepartment;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setWorknumber(String number){
        this.worknumber = number;
    }
    public void setWorkDepartmentCode(String code){
        this.workDepartmentCode = code;
    }
    public void setWorkId(String id){
        this.workId = id;
    }
    public void setWorkDepartmentID(String id){
        this.workDepartmentID = id;
    }
    public void setWorkmobile(String phone){
        this.workmobile = phone;
    }
    public void setWorktelephone(String phone){
        this.worktelephone = phone;
    }
    public void setWorkmail(String mail){
        this.workmail = mail;
    }
    public void setSex(int sex){
        this.sex = sex;
    }
    public void setIsmaindepartment(int main){
        this.ismaindepartment  = main;
    }
}
