package com.amr.prodconsumer.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class Sender {
    private Gson gson;
    private SimpMessagingTemplate simp;
    @Autowired
    public Sender(SimpMessagingTemplate SMTemplate){
        this.simp=SMTemplate;
        this.gson=new GsonBuilder().setPrettyPrinting().create();
    }
    public void send(update u){
        this.simp.convertAndSend("/sim/update",u);
        // this.simp.convertAndSend("/sim/update", (gson.toJson(u)));
    }
}
