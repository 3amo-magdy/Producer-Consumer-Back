package com.amr.prodconsumer.web;

import java.time.Clock;

public class update {
    String idQ;
    String idM;
    int qnumber;
    boolean mfree;
    long duration;
    String mColor;
    public update(String idQ, String idM, int qnumber, boolean mfree,String mC) {
        this.idQ = idQ;
        this.idM = idM;
        this.qnumber = qnumber;
        this.mfree = mfree;
        this.duration=Clock.systemDefaultZone().millis();
        this.mColor=mC;
    }
    public update(String idQ, String idM, int qnumber, boolean mfree) {
        this.idQ = idQ;
        this.idM = idM;
        this.qnumber = qnumber;
        this.mfree = mfree;
        this.duration=Clock.systemDefaultZone().millis();
    }
    public void time(long duration){
        this.duration = duration;
    }
    public String toString(){
        return("timestamp : "+this.duration+"\n"+
               "Q : " + this.idQ +"  "+ this.qnumber+"\n"+
               "M : " + this.idM +"  "+ this.mfree+" color ="+mColor+"\n" );
    }
    
}
