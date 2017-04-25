package com.lai.slinky.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/24.
 */
public class inform implements Parcelable {

    private int InformId;
    private String InformTitle;
    private String Content;
    private int PartyId;
    private String Memo;

    private String PartyName;

    public inform(){
    }

    public inform(int InformId, String InformTitle, String Content, int PartyId, String Memo){
        this.InformId = InformId;
        this.InformTitle = InformTitle;
        this.Content = Content;
        this.PartyId = PartyId;
        this.Memo = Memo;
    }

    public inform(int InformId, String InformTitle, String Content, String PartyName, String Memo){
        this.InformId = InformId;
        this.InformTitle = InformTitle;
        this.Content = Content;
        this.PartyName = PartyName;
        this.Memo = Memo;
    }

    public int getInformId() {
        return InformId;
    }

    public void setInformId(int informId) {
        InformId = informId;
    }

    public String getInformTitle() {
        return InformTitle;
    }

    public void setInformTitle(String informTitle) {
        InformTitle = informTitle;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getPartyId() {
        return PartyId;
    }

    public void setPartyId(int partyId) {
        PartyId = partyId;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public String getPartyName() {
        return PartyName;
    }

    public void setPartyName(String partyName) {
        PartyName = partyName;
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

        arg0.writeInt(InformId);
        arg0.writeString(InformTitle);
        arg0.writeString(Content);
        arg0.writeInt(PartyId);
        arg0.writeString(Memo);
        arg0.writeString(PartyName);

    }
    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下： 
    // android.os.BadParcelableException: 
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person 
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用 
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错； 
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错 
    // 5.反序列化对象 
    public static final Parcelable.Creator<inform> CREATOR = new Creator(){
        @Override
        public inform createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            //实现从source中创建出类的实例的功能
            inform p = new inform();
            p.setInformId(source.readInt());
            p.setInformTitle(source.readString());
            p.setContent(source.readString());
            p.setPartyId(source.readInt());
            p.setMemo(source.readString());
            p.setPartyName(source.readString());
            return p;
        }

        @Override
        public inform[] newArray(int size) {
            // TODO Auto-generated method stub
            return new inform[size];
        }
    };
}
