package com.lai.slinky.model;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/4/24.
 */
public class MarkObject {

    private Bitmap mBitmap;
    private float mapX;
    private float mapY;
    private MarkClickListener listener;

    private String ActName;
    private String Telephone;
    private String Notice;
    private String State;
    private String StartTime;//注意Date数据类型转换
    private String EndTime;
    private byte[] Poster;//因为传递Bitmap有限制大小，所以直接存储为Byte

    public MarkObject() {

    }

    public MarkObject(Bitmap mBitmap, float mapX, float mapY) {
        super();
        this.mBitmap = mBitmap;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    public MarkObject(String actName, String telephone, String notice, String state, String startTime, String endTime, byte[] poster) {
        ActName = actName;
        Telephone = telephone;
        Notice = notice;
        State = state;
        StartTime = startTime;
        EndTime = endTime;
        Poster = poster;
    }

    /**
     * @return the mBitmap
     */
    public Bitmap getmBitmap() {
        return mBitmap;
    }

    /**
     * @param mBitmap
     *            the mBitmap to set
     */
    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    /**
     * @return the mapX
     */
    public float getMapX() {
        return mapX;
    }

    /**
     * @param mapX
     *            the mapX to set
     */
    public void setMapX(float mapX) {
        this.mapX = mapX;
    }

    /**
     * @return the mapY
     */
    public float getMapY() {
        return mapY;
    }

    /**
     * @param mapY
     *            the mapY to set
     */
    public void setMapY(float mapY) {
        this.mapY = mapY;
    }

    public String getActName() {
        return ActName;
    }

    public void setActName(String actName) {
        ActName = actName;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getNotice() {
        return Notice;
    }

    public void setNotice(String notice) {
        Notice = notice;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public byte[] getPoster() {
        return Poster;
    }

    public void setPoster(byte[] poster) {
        Poster = poster;
    }

    public MarkClickListener getMarkListener() {
        return listener;
    }

    public void setMarkListener(MarkClickListener listener) {
        this.listener = listener;
    }

    public interface MarkClickListener {
        public void onMarkClick(int x, int y);
    }

}
