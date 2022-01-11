package com.amr.prodconsumer.Simulation;

import java.time.Clock;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.amr.prodconsumer.web.Sender;
import com.amr.prodconsumer.web.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@Component
public class tracker {
    boolean hasNew;
    LinkedList<update> updateQueue;
    private Stack<update> history;
    private long timeStamp;
    boolean on=true;

    @Autowired
    Sender sender;
    
    @Autowired
    public tracker(SimpMessagingTemplate SMTemplate){
        this.on=true;
        this.hasNew=false;
        this.updateQueue=new LinkedList<update>();
        this.history=new Stack<update>();
        this.timeStamp=Clock.systemDefaultZone().millis();
        this.sender=new Sender(SMTemplate);
    }
    
    // public tracker() {
	// }
    public Stack<update> getHistory(){
        return this.history;
    }
	public void update(update newUp) {
        newUp.time(Clock.systemDefaultZone().millis()-timeStamp);
        System.out.println(newUp.toString());
        this.hasNew=true;
        // this.updateQueue.add(newUp);
        // System.out.println("added an update to the trackerqueue");
        // System.out.println(this.updateQueue.size());
        this.timeStamp=Clock.systemDefaultZone().millis();
        System.out.println("tracker has an update");
        // this.time=Clock.systemDefaultZone().millis();
        // update u;
        // u = this.updateQueue.removeFirst();
        history.push(newUp);
        // if(Clock.systemDefaultZone().millis()-time>20){
        // System.out.println("tracker sending update -----");
        // System.out.println("the update is -----");
        // System.out.println(u.toString());
        // System.out.println("---------------------------");
        // time=Clock.systemDefaultZone().millis();
        // }
        this.sender.send(newUp);                    
    }
    public void reset(){
        this.hasNew=false;
    }

    public boolean hasUpdated(){
        return this.hasNew;
    }
    public void turnOff(){
        this.on =false;
    }
    void incrementTimeStamp(long time){
        timeStamp+=time;
    }
    void setTimeStamp(long time){
        timeStamp=time;
    }
    long getTimeStamp(){
        return timeStamp;
    }
    // @Override
    // public void run() {
    //     System.out.println("tracker started");
    //     long time=Clock.systemDefaultZone().millis();
    //     update u;
    //     while(this.on){
    //         if(this.updateQueue.size()!=0){
    //             System.out.println("queue has something");
    //             u = this.updateQueue.removeFirst();
    //             history.push(u);

    //             if(Clock.systemDefaultZone().millis()-time>20){
    //                 System.out.println("tracker sending update -----");
    //                 System.out.println("the update is -----");
    //                 System.out.println(u.toString());
    //                 System.out.println("---------------------------");
    //                 this.sender.send(u);                    
    //                 time=Clock.systemDefaultZone().millis();
    //             }
    //         }
    //         else{
    //             continue;
    //         }
    //     }
    //     System.out.println("TRACKER CLOSED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    // }
    
}
