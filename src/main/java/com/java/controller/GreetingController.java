package com.java.controller;


import com.java.domain.Message;
import com.java.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class GreetingController {

    private final MessageRepository messagesRepository;

    public GreetingController(MessageRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name, Map<String, Object> model) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping
    public String main(Map<String,Object> model){
        Iterable<Message> messages= messagesRepository.findAll();
        model.put("messages", messages);
        return "main";
    }

    @PostMapping
    public String add(@RequestParam String text, @RequestParam String tag,  Map<String,Object> model){
        Message message = new Message(text, tag);
        messagesRepository.save(message);
        Iterable<Message> messages= messagesRepository.findAll();
        model.put("messages", messages);
        return "main";
    }

    @PostMapping("filter")
        public String filter(@RequestParam String filter,   Map<String,Object> model){
        Iterable<Message> byTag;
        if(!filter.isEmpty() && filter !=null) {
            byTag = messagesRepository.findByTag(filter);
        } else {
            byTag = messagesRepository.findAll();
        }
        model.put("messages", byTag);
        return "main";
    }

}