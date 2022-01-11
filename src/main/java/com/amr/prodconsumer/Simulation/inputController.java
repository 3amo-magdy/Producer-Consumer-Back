package com.amr.prodconsumer.Simulation;

import java.time.Clock;
import java.util.Date;
import java.util.List;

import com.amr.prodconsumer.components.Q;
import com.amr.prodconsumer.web.update;

public class inputController extends Thread{
    int rate;
    Q q0;
    boolean running;
    private tracker tracker;
    private boolean pause;
    public inputController(tracker t){
        this.tracker=t;
        running =true;
    }
    public inputController(int ProductsPerMinute,Q q,tracker t){
        this.rate=ProductsPerMinute;
        this.q0=q;
        running =true;
        this.tracker=t;
    }

    public void setInputRate(int rate){
        System.out.println("input rate set to : "+rate);
        this.rate=rate;
    }
    public void setq0(Q q0){
        this.q0=q0;
    }
    public void turnOff(){
        this.running=false;
    }
    @Override
    public void run() {
        Long last = Clock.systemDefaultZone().millis();
        this.feedQ(q0, rate);
        while(running){
            if(!pause){
            if(Clock.systemDefaultZone().millis()-last>1000){
                this.feedQ(q0, rate);
                System.out.println("just fed q0");
                last=Clock.systemDefaultZone().millis();
            }}
        }
    }
    public boolean pause(){
        this.pause=true;
        return pause;
    }
    public boolean cont(){
        this.pause=false;
        return pause;
    }
    public void feedQ(Q q,int amount){
        q.addProducts(amount);
        update newUp=new update(q.getId().toString(),"input",q.getNumber(),false);
        this.tracker.sender.send(newUp);
        // this.tracker.update(newUp);
    }
}
