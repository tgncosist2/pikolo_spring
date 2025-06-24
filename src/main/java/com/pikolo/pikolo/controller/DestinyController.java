package com.pikolo.pikolo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.dto.destiny.DestinyDTO;
import com.pikolo.pikolo.service.destiny.DestinyService;
import com.pikolo.pikolo.service.springbootchatgpt.OpenAIService;



@RestController
@RequestMapping("/api/destinyResult")
public class DestinyController {
    
    @Autowired
    private OpenAIService openAi;

    @Autowired
    private DestinyService dService;

    @PostMapping
    public String sendData(@RequestBody DestinyDTO dDTO){
      
        String prompt = dService.makePrompt(dDTO);

        System.out.println(dDTO.getName());
        System.out.println(dDTO.getLanguage());
        System.out.println(prompt);

        String result = openAi.getChatGptResponse(prompt);
                
        System.out.println(result);
    
        return result;
    }
}
