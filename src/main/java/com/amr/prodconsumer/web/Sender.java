package com.amr.prodconsumer.web;

import java.util.Stack;

import com.amr.prodconsumer.Simulation.tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.converter.GsonMessageConverter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    private Gson gson;
    private SimpMessagingTemplate simp;
    @Autowired
    public Sender(SimpMessagingTemplate SMTemplate){
        this.simp=SMTemplate;
        this.gson=new GsonBuilder().setPrettyPrinting().create();
    }
    public void send(update u){
        System.out.println("sending update ..");
        // this.simp.convertAndSend("/sim/update",u);
        GsonMessageConverter mc = new GsonMessageConverter(gson);
        this.simp.setMessageConverter(mc);
        this.simp.convertAndSend("/sim/update", u);
    }
    // public void send(Stack<update> history){
    //     System.out.println("sending update ..");
    //     // this.simp.convertAndSend("/sim/update",u);
    //     GsonMessageConverter mc = new GsonMessageConverter(gson);
    //     this.simp.setMessageConverter(mc);
    //     this.simp.convertAndSend("/sim/update", u);
    // }

}
