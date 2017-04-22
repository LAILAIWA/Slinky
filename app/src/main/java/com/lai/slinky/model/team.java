package com.lai.slinky.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/5.
 */
public class team implements Parcelable {
    public static final String TAG = "Team";

    public static final String CLUB = "社团";

    public static final String CLASS = "班级";

    private int id;
    private String title;
    private String type;//组织类别：社团，班级
    private String charge1;
    private String info;
    private String partyPlace;
    private int partyNum;

    private Bitmap partyLogo;

    private String teacher;
    private String grade;
    public team(){
    }


    public team(int id,String title,String type,String charge1,String info,String partyPlace,int partyNum){
        this.id = id;
        this.title = title;
        this.type = type;
        this.charge1 = charge1;
        this.info = info;
        this.partyPlace = partyPlace;
        this.partyNum = partyNum;
    }

    public team(String info,Bitmap partyLogo){
        this.info = info;
        this.partyLogo = partyLogo;
    }

    public team(int id,String title,String type,String teacher,String grade){
        this.id = id;
        this.title = title;
        this.type = type;
        this.teacher = teacher;
        this.grade = grade;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCharge1(String charge1) {
        this.charge1 = charge1;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setPartyPlace(String partyPlace) {
        this.partyPlace = partyPlace;
    }

    public void setPartyNum(int partyNum) {
        this.partyNum = partyNum;
    }

    public void setPartyLogo(Bitmap partyLogo) {
        this.partyLogo = partyLogo;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getCharge1() {
        return charge1;
    }

    public String getInfo() {
        return info;
    }

    public String getPartyPlace() {
        return partyPlace;
    }

    public int getPartyNum() {
        return partyNum;
    }

    public Bitmap getPartyLogo() {
        return partyLogo;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        // TODO Auto-generated method stub
        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错 
        // 2.序列化对象 
        arg0.writeInt(id);
        arg0.writeString(title);
        arg0.writeString(type);
        arg0.writeString(charge1);
        arg0.writeString(info);
        arg0.writeString(partyPlace);
        arg0.writeInt(partyNum);
    }
    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下： 
    // android.os.BadParcelableException: 
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person 
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用 
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错； 
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错 
    // 5.反序列化对象 
    public static final Parcelable.Creator<team> CREATOR = new Creator(){
        @Override
        public team createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            team p = new team();
            p.setId(source.readInt());
            p.setTitle(source.readString());
            p.setType(source.readString());
            p.setCharge1(source.readString());
            p.setInfo(source.readString());
            p.setPartyPlace(source.readString());
            p.setPartyNum(source.readInt());
            return p;
        }

        @Override
        public team[] newArray(int size) {
            // TODO Auto-generated method stub
            return new team[size];
            }
    };
}
