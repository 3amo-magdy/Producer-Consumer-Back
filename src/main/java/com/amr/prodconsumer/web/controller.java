package com.amr.prodconsumer.web;

import java.util.UUID;

import com.amr.prodconsumer.Simulation.Simulator;
import com.amr.prodconsumer.web.GsonStrats.ExcludefromIn;
import com.amr.prodconsumer.web.GsonStrats.ExcludefromOut;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@RestController
@RequestMapping("/sim")
// @CrossOrigin("http://localhost:4200")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class controller {

    Simulator simulator;
    Gson gson;
    GsonBuilder builder;

    final ExclusionStrategy Outstrat=new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }
        @Override
        public boolean shouldSkipField(FieldAttributes arg0) {
            return (arg0.getAnnotation(ExcludefromOut.class)!=null);
        }   
    };
    final ExclusionStrategy Instrat=new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }
        @Override
        public boolean shouldSkipField(FieldAttributes arg0) {
            return (arg0.getAnnotation(ExcludefromIn.class)!=null);
        }   
    };
    SimpMessagingTemplate simp;
    @Autowired
    public controller(SimpMessagingTemplate simp){
        this.simulator=new Simulator( simp);
        builder=new GsonBuilder();
        builder.addSerializationExclusionStrategy(Outstrat).addDeserializationExclusionStrategy(Instrat);
        builder.setPrettyPrinting();
        gson=builder.create();
        this.simp=simp;
    }
    // @GetMapping
    // public String update(){
    //     if(this.tracker.hasUpdated()){
    //         return gson.toJson(this.simulator);
    //     }
    // }

    @GetMapping("/start")
    public String start(){
        return gson.toJson(this.simulator.startSimulating());
    }
    @GetMapping("/input/{inputRate}")
    public String startWithRate(@PathVariable int inputRate){
        this.simulator.setInputRate(inputRate);
        return gson.toJson("S");
    }
    @GetMapping("/input/{idq}/q0")
    public String setq0(@PathVariable String idq ){
        this.simulator.setQ0(idq);
        return gson.toJson("S");
    }
    @GetMapping("/input/{idq}/{input}")
    public String inputProducts(@PathVariable String idq,@PathVariable int input ){
        this.simulator.inputProducts(idq, input);
        return gson.toJson("S");
    }
    @GetMapping("/pause")
    public String pause(){
        this.simulator.pauseSimulating();
        return gson.toJson("S");
    }
    @GetMapping("/resume")
    public String resume(){
        this.simulator.resumeSimulating();
        return gson.toJson("S");
    }
    @GetMapping("/stop")
    public String stop(){
        this.simulator.stopSimulating();
        return gson.toJson("S");
    }
    @GetMapping("/addQ")
    public String addQ(){
        return gson.toJson(this.simulator.addQueue().getId());
    }
    @GetMapping("/addM")
    public String addM(){
        return gson.toJson(this.simulator.addService().getId());
    }
    @DeleteMapping("/removeQ/{id}")
    public String removeQ(@PathVariable String id){
        this.simulator.removeQueue(UUID.fromString(id));
        return gson.toJson("S");
    }
    @DeleteMapping("/removeM/{id}")
    public String removeM(@PathVariable String id){
        this.simulator.removeService(UUID.fromString(id));
        return gson.toJson("S");
    }
    @GetMapping("/linkQM/{idq}/{idm}")
    public String linkQM(@PathVariable String idq,@PathVariable String idm){
        System.out.println(idq);
        System.out.println(idm);
        try{this.simulator.linkProvider(UUID.fromString(idm),UUID.fromString(idq));
        }
        catch (Error e){
            return gson.toJson("F");
        }
        return gson.toJson("S");
    }
    @GetMapping("/linkMQ/{idm}/{idq}")
    public String linkMQ(@PathVariable String idm,@PathVariable String idq){
        try{this.simulator.setConsumer(UUID.fromString(idm),UUID.fromString(idq));}
        catch(Error e){
            return gson.toJson("F");
        }
        return gson.toJson("S");
    }
    @DeleteMapping("/delink/{idm}")
    public String delinkMQ(@PathVariable String idm){
        this.simulator.removeConsumer(UUID.fromString(idm));
        return gson.toJson("S");
    }
    @DeleteMapping("/delink/{idq}/{idm}")
    public String delinkQM(@PathVariable String idq,@PathVariable String idm){
        this.simulator.delinkProvider(UUID.fromString(idm),UUID.fromString(idq));
        return gson.toJson("S");
    }
    @GetMapping("/replay")
    public String replay(){
        return gson.toJson(this.simulator.replay());
    }
    @GetMapping("/new")
    public String newS(){
        this.simulator.stopSimulating();
        this.simulator =new Simulator(simp);
        return gson.toJson(this.simulator.getServices().size());
    }
    @GetMapping("/stopRate")
    public String stopRate(){
        return gson.toJson(this.simulator.stopRate());
    }
    @GetMapping("/conRate")
    public String conRate(){
        return gson.toJson(this.simulator.conRate());
    }
}
