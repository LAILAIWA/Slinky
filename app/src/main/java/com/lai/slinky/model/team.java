package com.lai.slinky.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/5.
 */
public class team implements Parcelable {
    public String title;
    public String type;
    public String charge1;
    public String charge2;
    public String info;
    public String place;
    public team(){
    }
    public team(String title,String type,String charge1){
        this.title = title;
        this.type = type;
        this.charge1 = charge1;
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

    public void setCharge2(String charge2) {
        this.charge2 = charge2;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public String getCharge2() {
        return charge2;
    }

    public String getInfo() {
        return info;
    }

    public String getPlace() {
        return place;
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
        arg0.writeString(title);
        arg0.writeString(type);
        arg0.writeString(info);
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
            p.setTitle(source.readString());
            p.setType(source.readString());
            p.setInfo(source.readString());
            return p;
        }

        @Override
        public team[] newArray(int size) {
            // TODO Auto-generated method stub
            return new team[size];
            }
    };
}
