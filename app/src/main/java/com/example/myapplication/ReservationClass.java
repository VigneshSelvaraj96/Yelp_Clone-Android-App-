package com.example.myapplication;

public class ReservationClass {
    private String name;
    private String emailid;
    private String date;
    private String time;

    public ReservationClass(String name, String emailid, String date, String time) {
        this.name = name;
        this.emailid = emailid;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
