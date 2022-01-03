package com.amr.prodconsumer.Simulation;

import java.util.ArrayList;

public class simulation {
    ArrayList<momento> simulation;
    public simulation(){
        this.simulation=new ArrayList<momento>();
    }
    public void addmomento(momento m){
        simulation.add(m);
    }
    public ArrayList<momento> getSimulation(){
        return this.simulation;
    }
}
