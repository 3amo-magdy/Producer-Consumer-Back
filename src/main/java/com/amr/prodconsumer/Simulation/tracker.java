package com.amr.prodconsumer.Simulation;

import java.util.Queue;

import com.amr.prodconsumer.web.Sender;
import com.amr.prodconsumer.web.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class tracker extends Thread{
    boolean hasNew;
    Queue<update> updateQueue;
    Sender sender;
    
    @Autowired
    public tracker(Sender sender){
        this.hasNew=false;
        this.sender=sender;
    }
    
    public tracker() {
	}

	public void update(update newUp) {
        this.hasNew=true;
        updateQueue.add(newUp);
    }

    public void reset(){
        this.hasNew=false;
    }

    public boolean hasUpdated(){
        return this.hasNew;
    }

    @Override
    public void run() {
        while(true){
            if(!updateQueue.isEmpty()){
                this.sender.send(updateQueue.poll());
                try {
                    wait(50);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
}
