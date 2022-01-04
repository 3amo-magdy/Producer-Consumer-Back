package com.amr.prodconsumer.web;

public class update {
    String idQ;
    String idM;
    int qnumber;
    boolean mfree;
    public update(String idQ, String idM, int qnumber, boolean mfree) {
        this.idQ = idQ;
        this.idM = idM;
        this.qnumber = qnumber;
        this.mfree = mfree;
    }
    public String toString(){
        return("Q : " + this.idQ +"  "+ this.qnumber+"\n"+
               "M : " + this.idM +"  "+ this.mfree);
    }
    
}
