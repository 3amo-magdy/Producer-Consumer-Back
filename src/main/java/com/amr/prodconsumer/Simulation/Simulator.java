package com.amr.prodconsumer.Simulation;

import java.util.ArrayList;
import java.util.UUID;

import com.amr.prodconsumer.components.M;
import com.amr.prodconsumer.components.Q;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@Component
public class Simulator {
    private ArrayList<M> services;
    public ArrayList<M> getServices() {
        return services;
    }
    public void setServices(ArrayList<M> services) {
        this.services = services;
    }
    private ArrayList<Thread> SThreads;
    private ArrayList<Q> queues;
    private ArrayList<UUID> ids;
    private inputController inputController;
    private tracker tracker;
    private boolean simulating; 
    private boolean pause; 

    
    @Autowired
    public Simulator(SimpMessagingTemplate SMTemplate){
        services=new ArrayList<M>();
        queues=new ArrayList<Q>();
        SThreads=new ArrayList<Thread>();
        ids= new ArrayList<UUID>();
        this.tracker=new tracker(SMTemplate);
        this.inputController=new inputController(this.tracker);
        simulating=false;
        pause=false;
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
        
        if(simulating){
            Thread thread = new Thread(m);
            this.SThreads.add(thread);
            thread.start();
            if(pause){
                m.pause();
            }
        }
        System.out.println(id.toString());
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
            if(m.getId().toString().equals(id.toString())){
                services.remove(m);
                return true;
            }
        }
        return false;
    }
    public M getService(UUID id){
        for (M m : services) {
            if(m.getId().toString().equals(id.toString())){
                return m;
            }
            else{
                System.out.println(m.getId());
            }
        }
        return null;
    }
    public boolean removeQueue(UUID id){
        if(simulating||pause){
            return false;
        }
        System.out.println(simulating);
        System.out.println(pause);
        boolean flag=false;
        for (Q q : queues) {
            if(q.getId().toString().equals(id.toString())){
                queues.remove(q);
                flag=true;
            }
        }
        for(M m:services){
            if(((Q)m.getConsumer()).getId().toString().equals(id.toString())){
                m.removeConsumer();
                flag=true;
            }
        }
        return false|flag;
    }
    public Q getQueue(UUID id){
        for (Q q : queues) {
            if(q.getId().toString().equals(id.toString())){
                return q;
            }
        }
        return null;
    }
    public void setInputRate(int rate){
        this.inputController.setInputRate(rate);
    }
    public void inputProducts(String Q_id,int amount){
        Q q=getQueue(UUID.fromString(Q_id));
        this.inputController.feedQ(q, amount);
    }
    public void setInputQueue(UUID id){
        Q q=getQueue(id);
        this.inputController.setq0(q);
    }
    public void linkProvider(UUID M_id,UUID Q_id){
        M m=getService(M_id);
        Q q=getQueue(Q_id);
        System.out.println(m);
        System.out.println(q);
        if(m.getConsumer()==q){
            throw new Error();
        }
        m.addObserver(q);
    }
    public void delinkProvider(UUID M_id,UUID Q_id){
        M m=getService(M_id);
        Q q=getQueue(Q_id);
        m.removeObserver(q);
    }
    public void setConsumer(UUID M_id,UUID Q_id){
        M m=getService(M_id);
        Q q=getQueue(Q_id);
        if(m.hasConsumer()||m.hasProvider(q)){
            throw new Error();
        }
        m.setConsumer(q);
    }
    public void removeConsumer(UUID M_id){
        M m=getService(M_id);
        m.removeConsumer();
    }
    
    public void startSimulating(){
        // inputController.start();
        if(simulating){
            return;
        }
        for(M m:services){
            Thread thread = new Thread(m);
            this.SThreads.add(thread);
        }
        for (Thread thread:SThreads) {
            thread.start();
        }
        // tracker.start();
        simulating=true;
    }
    public void stopSimulating(){
        simulating=false;
        for(M m:services){
            m.turnOff();
        }
        inputController.turnOff();
        tracker.turnOff();
    }
    public void pauseSimulating(){
        pause=true;
        for(M m:services){
            m.pause();
        }
    }
    public void resumeSimulating(){
        if(!pause)
            return;
        for(M m:services){
            m.resume();
        }
        pause=false;
    }
    public void setQ0(String idq) {
        Q q0= getQueue(UUID.fromString(idq));
        this.inputController.setq0(q0);
    }

}
