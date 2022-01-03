package com.amr.prodconsumer.Simulation;

import java.util.ArrayList;
import java.util.UUID;

import com.amr.prodconsumer.components.M;
import com.amr.prodconsumer.components.Q;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Simulator {
    private ArrayList<M> services;
    private ArrayList<Thread> SThreads;
    private ArrayList<Q> queues;
    private ArrayList<UUID> ids;
    private inputController inputController;
    private tracker tracker;
    private boolean simulating; 
    
    @Autowired
    public Simulator(){
        services=new ArrayList<M>();
        queues=new ArrayList<Q>();
        SThreads=new ArrayList<Thread>();
        ids= new ArrayList<UUID>();
        this.inputController=new inputController();
        this.tracker=new tracker();
        simulating=false;
    }
    public boolean isSimulating() {
        return simulating;
    }
    
    public M addService(){
        System.out.println("adding service:");
        
        UUID id;
        do{
            id=UUID.randomUUID();
        }
        while(ids.contains(id));
        ids.add(id);
        System.out.println("initing new M :");
        M m=new M(tracker,id);
        services.add(m);
        System.out.println("added and returning M :");
        return m;
    }
    public M addService(long time){
        UUID id;
        do{
            id=UUID.randomUUID();
        }
        while(ids.contains(id));
        ids.add(id);
        M m=new M(tracker,time,id);
        services.add(m);
        return m;
    }
    public void setServiceTime(UUID id,long t){
        M m =getService(id);
        m.setTime(t);
    }

    public Q addQueue(){
        UUID id;
        do{
            id=UUID.randomUUID();
        }
        while(ids.contains(id));
        ids.add(id);
        Q q=new Q(id);
        queues.add(q);
        return q;
    }
    public boolean removeService(UUID id){
        for (M m : services) {
            if(m.getId()==id){
                services.remove(m);
                return true;
            }
        }
        return false;
    }
    public M getService(UUID id){
        for (M m : services) {
            if(m.getId()==id){
                return m;
            }
        }
        return null;
    }
    public boolean removeQueue(UUID id){
        for (Q q : queues) {
            if(q.getId()==id){
                queues.remove(q);
                return true;
            }
        }
        return false;
    }
    public Q getQueue(UUID id){
        for (Q q : queues) {
            if(q.getId()==id){
                return q;
            }
        }
        return null;
    }
    public void setInputRate(int rate){
        this.inputController.setInputRate(rate);
    }
    public void setInputQueue(UUID id){
        Q q=getQueue(id);
        this.inputController.setq0(q);
    }
    public void linkProvider(UUID M_id,UUID Q_id){
        M m=getService(M_id);
        Q q=getQueue(Q_id);
        m.addObserver(q);
    }
    public void delinkProvider(UUID M_id,UUID Q_id){
        M m=getService(M_id);
        Q q=getQueue(Q_id);
        m.removeObserver(q);
    }
    public void setConsumer(UUID M_id,UUID Q_id){
        M m=getService(M_id);
        if(m.hasConsumer()){
            throw new Error();
        }
        Q q=getQueue(Q_id);
        m.setConsumer(q);
    }
    public void removeConsumer(UUID M_id){
        M m=getService(M_id);
        m.removeConsumer();
    }
    
    public void startSimulating(){
        tracker.start();
        inputController.start();
        for(M m:services){
            Thread thread = new Thread(m);
            this.SThreads.add(thread);
        }
        for (Thread thread:SThreads) {
            thread.start();
        }
        simulating=true;
    }

}
