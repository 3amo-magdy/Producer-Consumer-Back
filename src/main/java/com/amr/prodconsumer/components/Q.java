package com.amr.prodconsumer.components;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.amr.prodconsumer.observing.IObserver;

public class Q implements IObserver{
    private int number;
    private UUID id;
    private Long served;
    private List<String> colors;

    public Q(UUID id){
        this.setId(id);
        this.number=0;
        colors=new ArrayList<String>();
        this.served= -40L;
    }
    public Q(int n,UUID id){
        this.setId(id);
        this.number=n;
        colors=new ArrayList<String>();
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
    synchronized public ArrayList< Object> react1() {
        if(!(Clock.systemDefaultZone().millis()-served>40)){
            return null;
        }
        if(this.isEmpty()){
            return null;
        }
        String color=this.sendProduct();
        this.served=Clock.systemDefaultZone().millis();
        ArrayList<Object> ar=new ArrayList<>();
        ar.add(this.id);
        ar.add(color);
        ar.add(number);
        System.out.println(ar.toString());
        return  ar;
    }
    public List<String> getColors() {
        return colors;
    }
    public void setColors(List<String> colors) {
        this.colors = colors;
    }
    public int getRandomNumber() {
        return (int) ((Math.random() * (255 )));
    }
    public String generateRandomColor(){
        return "rgb("+getRandomNumber()+","+getRandomNumber()+","+getRandomNumber()+")";
    }
    public void addColor(){
       String color;
       do{ 
       color =generateRandomColor();
    }
    while(colors.contains(color));
    colors.add(color);
    System.out.println(color);
    System.out.println(colors.toString());
    }
    @Override
    public Object react2(String color) {
        this.addProduct(color);
        return this.number;
    }
    public int getNumber(){
        return number;
    }
    public String sendProduct(){
        this.number--;
        String color=null;
        System.out.println(number);
        if(colors.size()>0){
        color=colors.remove(0);
        }
        return color;
    }
    public void addProduct(String color){
        this.number++;
        colors.add(color);
    }
    public void addProducts(int amount){
        this.number+=amount;
        for(int i=0 ;i<amount;i++){
            addColor();
        }
    }    
}
