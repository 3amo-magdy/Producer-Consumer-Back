package com.amr.prodconsumer.web;

import java.time.Clock;

public class update {
    String idQ;
    String idM;
    int qnumber;
    boolean mfree;
    long duration;
    boolean st;
    int qTNumber;
    String mColor;
    public update(String idQ, String idM, int qnumber, boolean mfree,String mC,int qTNumber) {
        this.idQ = idQ;
        this.idM = idM;
        this.qnumber = qnumber;
        st=false;
        if(idM!=null){
            st=true;
        }
        this.mfree = mfree;
        this.duration=Clock.systemDefaultZone().millis();
        this.mColor=mC;
        this.qTNumber=qTNumber;
    }
    public update(String idQ, String idM, int qnumber, boolean mfree) {
        this.idQ = idQ;
        this.idM = idM;
        this.qTNumber=qnumber;
        this.mfree = mfree;
        this.duration=Clock.systemDefaultZone().millis();
    }
    public void time(long duration){
        this.duration = duration;
    }
    public long getDuration(){
        return this.duration;
    }
    public String getIdM(){
        return idM;
    }
    public String toString(){
        return("timestamp : "+this.duration+"\n"+
               "Q : " + this.idQ +"  "+ this.qnumber+"\n"+
               "M : " + this.idM +"  "+ this.mfree+"color: "+mColor+"qTNumber: "+qTNumber+"\n" );
    }
    
}
