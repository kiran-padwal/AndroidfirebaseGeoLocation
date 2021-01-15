package com.example.firebaseproductlist;

public class StoreModel {
    String sId,sName,sDes,sRadius,sLat,sLong;

    public StoreModel() {
    }

    public StoreModel(String sId, String sName, String sDes, String sRadius, String sLat, String sLong) {
        this.sId = sId;
        this.sName = sName;
        this.sDes = sDes;
        this.sRadius = sRadius;
        this.sLat = sLat;
        this.sLong = sLong;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsDes() {
        return sDes;
    }

    public void setsDes(String sDes) {
        this.sDes = sDes;
    }

    public String getsRadius() {
        return sRadius;
    }

    public void setsRadius(String sRadius) {
        this.sRadius = sRadius;
    }

    public String getsLat() {
        return sLat;
    }

    public void setsLat(String sLat) {
        this.sLat = sLat;
    }

    public String getsLong() {
        return sLong;
    }

    public void setsLong(String sLong) {
        this.sLong = sLong;
    }
}
