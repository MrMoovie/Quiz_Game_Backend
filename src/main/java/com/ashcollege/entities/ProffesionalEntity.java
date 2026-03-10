package com.ashcollege.entities;

public class ProffesionalEntity extends BasicUser{

    private int plan;
    private String areas;


    public int getPlan() {
        return plan;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public String getAreas() {
        return areas;
    }

    public void setAreas(String areas) {
        this.areas = areas;
    }
}
