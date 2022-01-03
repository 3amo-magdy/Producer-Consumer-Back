package com.amr.prodconsumer.components;

import java.io.InvalidClassException;
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
    private boolean free;
    private UUID id;

    public M(tracker t,UUID id){
        this.providers=new ArrayList<IObserver>();
        this.tracker=t;
        this.time=Double.doubleToLongBits(Math.random()*20);
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
        this.providers=new ArrayList<IObserver>();
        this.time=duration;
        free=true;
        on=true;
        this.setId(id);
    }
    public boolean hasConsumer(){
        return (this.consumer!=null);
    }
    public void setConsumer(IObserver o){
        this.consumer=o;
    }
    public void removeConsumer(){
        this.consumer=null;
    }
    
    @Override
    public void addObserver(IObserver o) {
        this.providers.add(o);
    }
    
    @Override
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
        while(on){
            Object res = notifyObservers();
            if(res!=null){
                this.free=false;
                update newUp=new update(((UUID)res).toString(),this.id.toString(),-1,false);
                this.tracker.update(newUp);
                try {
                    wait(this.time);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                this.sendProduct();
                update newUp2=new update((((Q)this.consumer).getId()).toString(),this.id.toString(),+1,true);
                this.tracker.update(newUp2);
                this.free=true;

            }
            else if(!isFree()){
                //if all providers were out of products.
                this.free=true;
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
        if(response!=null&&response instanceof Boolean){
            return response;
        }
        else{
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
