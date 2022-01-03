package com.amr.prodconsumer.components;

import java.util.UUID;

import com.amr.prodconsumer.observing.IObserver;

public class Q implements IObserver{
    private int number;
    private UUID id;
    
    public Q(UUID id){
        this.setId(id);
        this.number=0;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public Q(int n,UUID id){
        this.setId(id);
        this.number=n;
    }
    
    public boolean isEmpty(){
        return (this.number==0);
    }

    @Override
    public Object react1() {
        if(this.isEmpty()){
            return null;
        }
        this.sendProduct();
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
