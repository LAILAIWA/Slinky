package com.lai.slinky.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/17.
 */
public class activityy implements Parcelable {
    private int ActId;
    private String ActName;
//    private Bitmap Poster;
    private byte[] Poster;//因为传递Bitmap有限制大小，所以直接存储为Byte
    private int PartyId;
    private String Organizer;
    private String Telephone;
    private String Notice;
    private int BuildingId;
    private String ActPlace;
    private int ActNumber;
    private int StateId;
    private String Memo;
    private String StartTime;//注意Date数据类型转换
    private String EndTime;

    public activityy(){
    }

    public activityy(int actId, String actName, int partyId, String organizer, String telephone, String notice, int buildingId, String actPlace, int actNumber, int stateId, String memo, String StartTime, String EndTime) {
        ActId = actId;
        ActName = actName;
        PartyId = partyId;
        Organizer = organizer;
        Telephone = telephone;
        Notice = notice;
        BuildingId = buildingId;
        ActPlace = actPlace;
        ActNumber = actNumber;
        StateId = stateId;
        Memo = memo;
    }

    public int getActId() {
        return ActId;
    }

    public String getActName() {
        return ActName;
    }

    public byte[] getPoster() {
        return Poster;
    }

    public int getPartyId() {
        return PartyId;
    }

    public String getOrganizer() {
        return Organizer;
    }

    public String getTelephone() {
        return Telephone;
    }

    public String getNotice() {
        return Notice;
    }

    public int getBuildingId() {
        return BuildingId;
    }

    public String getActPlace() {
        return ActPlace;
    }

    public int getActNumber() {
        return ActNumber;
    }

    public int getStateId() {
        return StateId;
    }

    public String getMemo() {
        return Memo;
    }

    public String getStartTime() {
        return StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setActId(int actId) {
        ActId = actId;
    }

    public void setActName(String actName) {
        ActName = actName;
    }

    public void setPoster(byte[] poster) {
        Poster = poster;
    }

    public void setPartyId(int partyId) {
        PartyId = partyId;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public void setOrganizer(String organizer) {
        Organizer = organizer;
    }

    public void setNotice(String notice) {
        Notice = notice;
    }

    public void setBuildingId(int buildingId) {
        BuildingId = buildingId;
    }

    public void setActPlace(String actPlace) {
        ActPlace = actPlace;
    }

    public void setActNumber(int actNumber) {
        ActNumber = actNumber;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public void setStateId(int stateId) {
        StateId = stateId;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        // TODO Auto-generated method stub
        //该方法将类的数据写入外部提供的Parcel中
        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错 
        // 2.序列化对象 
        //图片poster单独传递
        arg0.writeInt(ActId);
        arg0.writeString(ActName);

        arg0.writeInt(PartyId);
        arg0.writeString(Organizer);
        arg0.writeString(Telephone);
        arg0.writeString(Notice);
        arg0.writeInt(BuildingId);
        arg0.writeString(ActPlace);
        arg0.writeInt(ActNumber);
        arg0.writeInt(StateId);
        arg0.writeString(Memo);
        arg0.writeString(StartTime);
        arg0.writeString(EndTime);
    }
    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下： 
    // android.os.BadParcelableException: 
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person 
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用 
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错； 
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错 
    // 5.反序列化对象 
    public static final Parcelable.Creator<activityy> CREATOR = new Creator(){
        @Override
        public activityy createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            //实现从source中创建出类的实例的功能
            activityy p = new activityy();
            p.setActId(source.readInt());
            p.setActName(source.readString());

            p.setPartyId(source.readInt());
            p.setOrganizer(source.readString());
            p.setTelephone(source.readString());
            p.setNotice(source.readString());
            p.setBuildingId(source.readInt());
            p.setActPlace(source.readString());
            p.setActNumber(source.readInt());
            p.setStateId(source.readInt());
            p.setMemo(source.readString());
            p.setStartTime(source.readString());
            p.setEndTime(source.readString());
            return p;
        }

        @Override
        public activityy[] newArray(int size) {
            // TODO Auto-generated method stub
            return new activityy[size];
        }
    };
}
