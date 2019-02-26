package com.carlgira.soa.managers.audit;


/**
 * Created by cgiraldo on 04/05/2017.
 */
public class CEvent {

    private String date;
    private String state;

    public CEvent(){
    }

    public String getDate() {
        return date;
    }

    public String getState() {
        return state;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setState(String state) {
        this.state = state;
    }
}
