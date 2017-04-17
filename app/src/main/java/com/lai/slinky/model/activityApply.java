package com.lai.slinky.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/17.
 */
public class activityApply implements Parcelable {
    private int ActApplyId;
    private String PartyChargeId;
    private String ActChargeId;
    private int PartyId;
    private String ActName;
    private String PartyChargeDuty;
    private String ActChargeDuty;
    private String PartyChargePhone;
    private String ActChargePhone;
    private String ActContent;
    private int StateId;

    public activityApply(){
    }
    public activityApply(int actApplyId, String partyChargeId, String actChargeId, int partyId, String actName, String partyChargeDuty, String actChargeDuty, String partyChargePhone, String actChargePhone, String actContent, int stateId) {
        ActApplyId = actApplyId;
        PartyChargeId = partyChargeId;
        ActChargeId = actChargeId;
        PartyId = partyId;
        ActName = actName;
        PartyChargeDuty = partyChargeDuty;
        ActChargeDuty = actChargeDuty;
        PartyChargePhone = partyChargePhone;
        ActChargePhone = actChargePhone;
        ActContent = actContent;
        StateId = stateId;
    }

    public int getActApplyId() {
        return ActApplyId;
    }

    public String getPartyChargeId() {
        return PartyChargeId;
    }

    public String getActChargeId() {
        return ActChargeId;
    }

    public int getPartyId() {
        return PartyId;
    }

    public String getActName() {
        return ActName;
    }

    public String getPartyChargeDuty() {
        return PartyChargeDuty;
    }

    public String getActChargeDuty() {
        return ActChargeDuty;
    }

    public String getPartyChargePhone() {
        return PartyChargePhone;
    }

    public String getActChargePhone() {
        return ActChargePhone;
    }

    public String getActContent() {
        return ActContent;
    }

    public int getStateId() {
        return StateId;
    }

    public void setActApplyId(int actApplyId) {
        ActApplyId = actApplyId;
    }

    public void setPartyChargeId(String partyChargeId) {
        PartyChargeId = partyChargeId;
    }

    public void setActChargeId(String actChargeId) {
        ActChargeId = actChargeId;
    }

    public void setPartyId(int partyId) {
        PartyId = partyId;
    }

    public void setPartyChargeDuty(String partyChargeDuty) {
        PartyChargeDuty = partyChargeDuty;
    }

    public void setActName(String actName) {
        ActName = actName;
    }

    public void setActChargeDuty(String actChargeDuty) {
        ActChargeDuty = actChargeDuty;
    }

    public void setPartyChargePhone(String partyChargePhone) {
        PartyChargePhone = partyChargePhone;
    }

    public void setActChargePhone(String actChargePhone) {
        ActChargePhone = actChargePhone;
    }

    public void setActContent(String actContent) {
        ActContent = actContent;
    }

    public void setStateId(int stateId) {
        StateId = stateId;
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
        arg0.writeInt(ActApplyId);
        arg0.writeString(PartyChargeId);
        arg0.writeString(ActChargeId);
        arg0.writeInt(PartyId);
        arg0.writeString(ActName);
        arg0.writeString(PartyChargeDuty);
        arg0.writeString(ActChargeDuty);
        arg0.writeString(PartyChargePhone);
        arg0.writeString(ActChargeDuty);
        arg0.writeString(ActContent);
        arg0.writeInt(StateId);
    }
    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下： 
    // android.os.BadParcelableException: 
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person 
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用 
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错； 
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错 
    // 5.反序列化对象 
    public static final Parcelable.Creator<activityApply> CREATOR = new Creator(){
        @Override
        public activityApply createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            activityApply p = new activityApply();
            p.setActApplyId(source.readInt());
            p.setPartyChargeId(source.readString());
            p.setActChargeId(source.readString());
            p.setPartyId(source.readInt());
            p.setActName(source.readString());
            p.setPartyChargeDuty(source.readString());
            p.setActChargeDuty(source.readString());
            p.setPartyChargePhone(source.readString());
            p.setActChargePhone(source.readString());
            p.setActContent(source.readString());
            p.setStateId(source.readInt());
            return p;
        }

        @Override
        public activityApply[] newArray(int size) {
            // TODO Auto-generated method stub
            return new activityApply[size];
        }
    };
}
