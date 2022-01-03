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

    public controller(){
        this.simulator=new Simulator();
        builder=new GsonBuilder();
        builder.addSerializationExclusionStrategy(Outstrat).addDeserializationExclusionStrategy(Instrat);
        builder.setPrettyPrinting();
        gson=builder.create();
    }
    // @GetMapping
    // public String update(){
    //     if(this.tracker.hasUpdated()){
    //         return gson.toJson(this.simulator);
    //     }
    // }

    @GetMapping("/addQ")
    public String addQ(){
        return gson.toJson(this.simulator.addQueue());
    }
    @GetMapping("/addM")
    public String addM(){
        return gson.toJson(this.simulator.addService());
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
        this.simulator.linkProvider(UUID.fromString(idm),UUID.fromString(idq));
        return gson.toJson("S");
    }
    @GetMapping("/linkMQ/{idm}/{idq}")
    public String linkMQ(@PathVariable String idq,@PathVariable String idm){
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
    // @DeleteMapping("/delink/{id}")
    // public String removeQ(@PathVariable String id){
    //     this.simulator.removeQueue(UUID.fromString(id))
    //     return gson.toJson("S");
    // }
    


}
