package com.amr.prodconsumer.components;

import java.time.Clock;
import java.util.UUID;

import com.amr.prodconsumer.observing.IObserver;

public class Q implements IObserver{
    private int number;
    private UUID id;
    private Long served;

    public Q(UUID id){
        this.setId(id);
        this.number=0;
        this.served= -40L;
    }
    public Q(int n,UUID id){
        this.setId(id);
        this.number=n;
        this.served= -40L;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    
    public boolean isEmpty(){
        return (this.number<=0);
    }

    @Override
    synchronized public Object react1() {
        if(!(Clock.systemDefaultZone().millis()-served>40)){
            return null;
        }
        if(this.isEmpty()){
            return null;
        }
        this.sendProduct();
        this.served=Clock.systemDefaultZone().millis();
        return this.id;
    }

    @Override
    public Object react2() {
        this.addProduct();
        return true;
    }

    public void sendProduct(){
        this.number--;
    }
    public void addProduct(){
        this.number++;
    }
    public void addProducts(int amount){
        this.number+=amount;
    }

    
}
