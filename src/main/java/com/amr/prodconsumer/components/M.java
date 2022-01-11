package com.amr.prodconsumer.components;

import java.io.InvalidClassException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.UUID;

import com.amr.prodconsumer.Simulation.tracker;
import com.amr.prodconsumer.observing.IObservable;
import com.amr.prodconsumer.observing.IObserver;
import com.amr.prodconsumer.web.update;
import com.amr.prodconsumer.web.GsonStrats.*;

public class M implements IObservable,Runnable{
    // the queues that provide M with products
    private ArrayList<IObserver> providers;
    private IObserver consumer;
    @ExcludefromOut
    private tracker tracker;
    @ExcludefromOut
    private boolean on;

    String currentColor;
    private long time;
    private long restTime;
    private long pauseTimeStamp;
    private long pauseTime;
    private boolean free;
    private boolean paused;
    private UUID id;

    public M(tracker t,UUID id){
        this.providers=new ArrayList<IObserver>();
        this.tracker=t;
        this.time=Math.round(Math.random()*1000+500);
        
        System.out.println("M's time :"+time);
        this.restTime=300;
        this.pauseTime=0;
        free=true;
        on=true;
        paused=false;
        this.pauseTime=0;
        currentColor="rgb(0,128,0)";
        this.setId(id);
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public M(tracker t, long duration,UUID id){
        this.providers=new ArrayList<IObserver>();
        this.tracker=t;
        this.time=duration;
        this.restTime=300;
        free=true;
        on=true;
        paused=false;
        this.pauseTime=0;
        this.setId(id);
    }
    public boolean hasConsumer(){
        return (this.consumer!=null);
    }
    public boolean hasProvider(IObserver o){
        return (this.providers.contains(o));
    }
    public void setConsumer(IObserver o){
        this.consumer=o;
    }
    public IObserver getConsumer(){
        return this.consumer;
    }
    public void removeConsumer(){
        this.consumer=null;
    }
    
    public void addObserver(IObserver o) {
        this.providers.add(o);
    }
    
    public void removeObserver(IObserver o) {
        this.providers.remove(o);        
    }
    public void turnOff(){
        this.on=false;
    }
    public boolean isFree(){
        return this.free;
    }
    public void pause(){
        this.paused=true;
        this.pauseTimeStamp=Clock.systemDefaultZone().millis();
    }
    public void resume(){
        this.paused=false;
        this.pauseTime=Clock.systemDefaultZone().millis()-pauseTimeStamp;
    }
    @Override
    public void run() {
        long timeStamp;
        timeStamp=Clock.systemDefaultZone().millis();
        while(on){
            if(free){
                    if(Clock.systemDefaultZone().millis()-timeStamp>this.restTime){
                        ArrayList<Object> res = notifyObservers();
                        if(res != null){
                            currentColor=(String)res.get(1);
                           int total =(int)res.get(2);
                            System.out.println(total);
                            System.out.println(currentColor);   
                            update newUp=new update(((UUID)(res.get(0))).toString(),this.id.toString(),-1,false,currentColor,total);
                            this.tracker.update(newUp);
                            timeStamp=Clock.systemDefaultZone().millis();
                            System.out.println("lol");
                            this.free=false;
                        }
                }
            }
            else{
                if(!paused&&Clock.systemDefaultZone().millis()-timeStamp>this.time+this.pauseTime){
                    if(!hasConsumer()){
                        continue;
                    }
                    int number=this.sendProduct();
                    update newUp2=new update((((Q)this.consumer).getId()).toString(),this.id.toString(),+1,true,currentColor,number);
                    this.tracker.update(newUp2);
                    System.out.println(tracker.getHistory().toString());
                    this.free=true;
                    timeStamp=Clock.systemDefaultZone().millis();
                    this.pauseTime=0;
                }
            }
        }
    }
    @Override
    public  ArrayList< Object> notifyObservers() {
        for (IObserver o : providers) {
            try {
                ArrayList< Object> id_q=notifyObserver(o);
                if(id_q!=null){
                    return id_q;
                }
            } catch (InvalidClassException e) {
                //  Handle Other responses types when added in the future.
            }
        }
        //if all providers were out of products
        return null;
    }

    public ArrayList< Object> notifyObserver(IObserver o) throws InvalidClassException {
        ArrayList< Object> response =o.react1();
        if(response == null || response.get(0) instanceof UUID){
            return response;
        }
        else{
            System.out.println(response);
            throw new InvalidClassException(response.getClass().toString()+" Is Not Supported As A Response Type");
        }
    }

    public int sendProduct(){
        int number =(int) this.consumer.react2(currentColor);
        return number;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }

    
    
}
