package com.amr.prodconsumer.Simulation;

import java.time.Clock;
import java.util.Date;

import com.amr.prodconsumer.components.Q;

public class inputController extends Thread{
    int rate;
    Q q0;
    boolean running;
    
    public inputController(){
        running =true;
    }
    public inputController(int ProductsPerMinute,Q q){
        this.rate=ProductsPerMinute;
        this.q0=q;
        running =true;
    }

    public void setInputRate(int rate){
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
            if(Clock.systemDefaultZone().millis()-last>1000){
                this.feedQ(q0, rate);
            }
        }
    }
    public void feedQ(Q q,int amount){
        q.addProducts(amount);
    }
    
}
