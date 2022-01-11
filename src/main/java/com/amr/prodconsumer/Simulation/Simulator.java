package com.amr.prodconsumer.Simulation;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;

import com.amr.prodconsumer.components.M;
import com.amr.prodconsumer.components.Q;
import com.amr.prodconsumer.web.update;

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
    private long pauseSt;
    private long pauseTotal;

    
    @Autowired
    public Simulator(SimpMessagingTemplate SMTemplate){
        services=new ArrayList<M>();
        queues=new ArrayList<Q>();
        SThreads=new ArrayList<Thread>();
        ids= new ArrayList<UUID>();
        System.out.println(SMTemplate.getMessageChannel().toString());
        this.tracker=new tracker(SMTemplate);
        this.inputController=new inputController(this.tracker);
        simulating=false;
        pause=false;
    }
    public boolean isSimulating() {
        return simulating;
    }
    public Stack<update>replay(){
        return tracker.getHistory();
    }
    public Stack<update>replayO(){
        Stack<update> answer=new Stack<>();
        int n=tracker.getHistory().size();
        if(n<2)return null;
     
        String wantReplayMachine=((update)(tracker.getHistory().get(n-1))).getIdM();
        int i;
        for(i=n-2;i>=0;i--){
            if((((update)(tracker.getHistory().get(i))).getIdM()).equals(wantReplayMachine)){
                break;
            }
            else if((((update)(tracker.getHistory().get(i))).getIdM()).equals("before")){
                return tracker.getHistory();
            }
        }
        for(;i<n;i++){
            answer.push(((update)(tracker.getHistory().get(i))));
        }
        return answer;
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
    public boolean stopRate(){
        return this.inputController.pause();
    }
    public boolean conRate(){
        return this.inputController.cont();
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
    
    public String startSimulating(){

        if(simulating){
            return "Running";
        }
        for(M m:services){
           if( !m.hasConsumer()){
            return "cant start";
           }
        }
        inputController.start();
        tracker.setTimeStamp(Clock.systemDefaultZone().millis());
        for(Q q:queues){
            tracker.update(new update(q.getId().toString(), "before", q.getNumber(), false));
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
        return "Started";
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
        pauseSt=Clock.systemDefaultZone().millis();
        this.inputController.pause();
        for(M m:services){
            m.pause();
        }
    }
    public void resumeSimulating(){
        if(!pause)
            return;
        pauseTotal=Clock.systemDefaultZone().millis()-pauseSt;
        // update u=tracker.getHistory().get(tracker.getHistory().size()-1);
        // u.time(pauseTotal-u.getDuration());
        tracker.incrementTimeStamp(pauseTotal);
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
