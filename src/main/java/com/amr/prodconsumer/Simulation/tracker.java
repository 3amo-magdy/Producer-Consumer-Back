package com.amr.prodconsumer.Simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.amr.prodconsumer.web.Sender;
import com.amr.prodconsumer.web.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class tracker extends Thread{
    boolean hasNew;
    Queue<update> updateQueue;
    public Stack<update> history;
    @Autowired
    Sender sender;
    
    @Autowired
    public tracker(){
        this.hasNew=false;
        this.updateQueue=new LinkedList<update>();
        this.history=new Stack<update>();
    }
    
    // public tracker() {
	// }

	public void update(update newUp) {
        System.out.println(newUp.toString());
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
                update u = this.updateQueue.poll();
                history.push(u);
                System.out.println(history.toString());
                this.sender.send(u);
                try {
                    wait(20);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
}
