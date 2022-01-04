package com.amr.prodconsumer.Simulation;

import java.time.Clock;
import java.util.Date;

import com.amr.prodconsumer.components.Q;
import com.amr.prodconsumer.web.update;

public class inputController extends Thread{
    int rate;
    Q q0;
    boolean running;
    private tracker tracker;

    
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
        while(running){
            if(Clock.systemDefaultZone().millis()-last>60000){
                this.feedQ(q0, rate);
                System.out.println("just fed q0");
                last=Clock.systemDefaultZone().millis();
            }
        }
    }
    public void feedQ(Q q,int amount){
        q.addProducts(amount);
        update newUp=new update(q.getId().toString(),"input",amount,false);
        this.tracker.update(newUp);
    }
}
