package com.example.alejandro.d_it;

/**
 * Created by MacBook on 27/03/16.
 */
public class Dates {

    String patient;
    String day;
    String hour;
    String status;

    public Dates(String patient, String day, String hour, String status) {
        this.patient = patient;
        this.day = day;
        this.hour = hour;
        this.status = status;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
