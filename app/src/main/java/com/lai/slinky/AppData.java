package com.lai.slinky;

import android.app.Application;

import com.lai.slinky.model.activityy;
import com.lai.slinky.model.inform;
import com.lai.slinky.model.team;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/2.
 */
public class AppData extends Application {
    //定义软件全局变量
    ArrayList<inform> listDataInform = new ArrayList<inform>();
    ArrayList<team> listDataAllClub = new ArrayList<team>();
    ArrayList<byte[]> LogoArray = new ArrayList<byte[]>();

    ArrayList<activityy> listDataActivityForMap = new ArrayList<activityy>();
    ArrayList<byte[]> PosterArrayForMap = new ArrayList<byte[]>();

    public ArrayList<inform> getListDataInform() {
        return listDataInform;
    }

    public void setListDataInform(ArrayList<inform> listDataInform) {
        this.listDataInform = listDataInform;
    }

    public ArrayList<team> getListDataAllClub() {
        return listDataAllClub;
    }

    public void setListDataAllClub(ArrayList<team> listDataAllClub) {
        this.listDataAllClub = listDataAllClub;
    }

    public ArrayList<byte[]> getLogoArray() {
        return LogoArray;
    }

    public void setLogoArray(ArrayList<byte[]> logoArray) {
        LogoArray = logoArray;
    }

    public ArrayList<activityy> getListDataActivityForMap() {
        return listDataActivityForMap;
    }

    public void setListDataActivityForMap(ArrayList<activityy> listDataActivityForMap) {
        this.listDataActivityForMap = listDataActivityForMap;
    }

    public ArrayList<byte[]> getPosterArrayForMap() {
        return PosterArrayForMap;
    }

    public void setPosterArrayForMap(ArrayList<byte[]> posterArrayForMap) {
        PosterArrayForMap = posterArrayForMap;
    }
}
