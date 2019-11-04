package com.thanhhuy.mapboxdemoapp.Model;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class Data {
    private LatLng point;
    private int temp, humid;

    public Data(LatLng point,int temp, int humid){
        this.point = point;
        this.temp = temp;
        this.humid = humid;

    }

    public void setPoint(LatLng point) {
        this.point = point;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public void setHumid(int humid) {
        this.humid = humid;
    }

    public LatLng getPoint() {
        return point;
    }

    public int getTemp() {
        return temp;
    }

    public int getHumid() {
        return humid;
    }
}
