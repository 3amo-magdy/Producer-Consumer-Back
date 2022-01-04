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
    @ExcludefromOut
    private ArrayList<IObserver> providers;
    @ExcludefromOut
    private IObserver consumer;
    @ExcludefromOut
    private tracker tracker;
    @ExcludefromOut
    private boolean on;


    private long time;
    private long restTime;
    private boolean free;
    private UUID id;

    public M(tracker t,UUID id){
        this.providers=new ArrayList<IObserver>();
        this.tracker=t;
        this.time=Double.doubleToLongBits(Math.random()*8000);
        this.restTime=300;
        free=true;
        on=true;
        this.setId(id);
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public M(tracker t, long duration,UUID id){
        this.tracker=t;
        this.providers=new ArrayList<IObserver>();
        this.time=duration;
        this.restTime=300;
        free=true;
        on=true;
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
    @Override
    public void run() {
        long timeStamp;
        timeStamp=Clock.systemDefaultZone().millis();
        while(on){
            if(free){
                    if(Clock.systemDefaultZone().millis()-timeStamp>this.restTime){
                        Object res = notifyObservers();
                        if(res==null){
                            System.out.println("Machine "+this.id+" is waiting");
                            continue;
                        }
                        update newUp=new update(((UUID)res).toString(),this.id.toString(),-1,false);
                        this.tracker.update(newUp);
                        timeStamp=Clock.systemDefaultZone().millis();
                        System.out.println("lol");
                        this.free=false;
                }
            }
            else{
                if(Clock.systemDefaultZone().millis()-timeStamp>this.time){
                    this.sendProduct();
                    update newUp2=new update((((Q)this.consumer).getId()).toString(),this.id.toString(),+1,true);
                    this.tracker.update(newUp2);
                    System.out.println(tracker.history.toString());
                    this.free=true;
                    timeStamp=Clock.systemDefaultZone().millis();
                }

            }
        }
    }
    @Override
    public Object notifyObservers() {
        for (IObserver o : providers) {
            try {
                Object id_q=notifyObserver(o);
                if(id_q!=null){
                    return id_q;
                }
            } catch (InvalidClassException e) {
                //  Handle Other responses types when added in the future.
            }
        }
        return null;
        
    }

    public Object notifyObserver(IObserver o) throws InvalidClassException {
        Object response =o.react1();
        if(response == null || response instanceof UUID){
            return response;
        }
        else{
            System.out.println(response);
            throw new InvalidClassException(response.getClass().toString()+" Is Not Supported As A Response Type");
        }
    }

    public void sendProduct(){
        this.consumer.react2();
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }

    
    
}
