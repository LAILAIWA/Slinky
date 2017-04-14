package com.lai.slinky.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/8.
 */
public class apply implements Parcelable {
    private String Id;
    private String Name;
    private int Grade;
    private String Classs;
    private String Telephone;
    private String Interesting;
    private String JoinReason;
    private int State;
    private int PartyId;
    private String ExitReason;

    public apply(){
    }
    //入团申请的构造方法
    public apply(String Id,String Name,int Grade,String Classs,String Telephone,String Interesting,String JoinReason,int State){
        this.Id = Id;
        this.Name = Name;
        this.Grade = Grade;
        this.Classs = Classs;
        this.Telephone = Telephone;
        this.Interesting = Interesting;
        this.JoinReason = JoinReason;
        this.State = State;
    }
    //退团申请的构造方法
    public apply(String Id,String Name,String ExitReason,int State){
        this.Id = Id;
        this.Name = Name;
        this.Grade = Grade;
        this.ExitReason = ExitReason;
        this.State = State;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setExitReason(String exitReason) {
        ExitReason = exitReason;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setClasss(String Classs) {
        Classs = Classs;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public void setInteresting(String interesting) {
        Interesting = interesting;
    }

    public void setJoinReason(String joinReason) {
        JoinReason = joinReason;
    }

    public void setState(int state) {
        this.State = State;
    }

    public void setPartyId(int partyId) {
        PartyId = partyId;
    }

    public void setGrade(int grade) {

        Grade = grade;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public int getGrade() {
        return Grade;
    }

    public String getClasss() {
        return Classs;
    }

    public String getTelephone() {
        return Telephone;
    }

    public String getInteresting() {
        return Interesting;
    }

    public String getJoinReason() {
        return JoinReason;
    }

    public int getState() {
        return State;
    }

    public int getPartyId() {
        return PartyId;
    }

    public String getExitReason() {
        return ExitReason;
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

        arg0.writeString(Id);
        arg0.writeString(Name);
        arg0.writeInt(Grade);
        arg0.writeString(Classs);
        arg0.writeString(Telephone);
        arg0.writeString(Interesting);
        arg0.writeString(JoinReason);
        arg0.writeInt(State);
        arg0.writeInt(PartyId);
        arg0.writeString(ExitReason);
    }
    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下： 
    // android.os.BadParcelableException: 
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person 
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用 
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错； 
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错 
    // 5.反序列化对象 
    public static final Parcelable.Creator<apply> CREATOR = new Creator(){
        @Override
        public apply createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            apply p = new apply();
            p.setId(source.readString());
            p.setName(source.readString());
            p.setGrade(source.readInt());
            p.setClasss(source.readString());
            p.setTelephone(source.readString());
            p.setInteresting(source.readString());
            p.setJoinReason(source.readString());
            p.setState(source.readInt());
            p.setPartyId(source.readInt());
            p.setExitReason(source.readString());
            return p;
        }

        @Override
        public apply[] newArray(int size) {
            // TODO Auto-generated method stub
            return new apply[size];
        }
    };
}
