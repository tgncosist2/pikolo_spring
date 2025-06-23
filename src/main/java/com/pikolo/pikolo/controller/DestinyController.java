package com.pikolo.pikolo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.dto.destiny.DestinyDTO;
import com.pikolo.pikolo.service.springbootchatgpt.OpenAIService;



@RestController
@RequestMapping("/api/destinyResult")
public class DestinyController {
    
    @Autowired
    private OpenAIService openAi;

    @PostMapping
    public String sendData(@RequestBody DestinyDTO dDTO){

        String birthPrompt = "";
      if(dDTO.getBirthTime().equals("모름")){
            birthPrompt = "태어난 시각은 모르지만 태어난 시각 모르는 것을 감안해주고";
      }else{
        birthPrompt= dDTO.getBirthTime();
      }
      String prompt="이름 : " + dDTO.getName()+" 생년월일 : "+dDTO.getBirthDate()
        +" 양력/음력(평달)/음력(윤달) : "+dDTO.getCalendar()+
        " 성별 : "+dDTO.getGender()+" 태어난시각 : "+ birthPrompt
        +" 이 정보들로 재미로보는 평생사주 총평, 초년운, 중년운, 말년운, 건강운, 이성운, 재물운,재물모으는법 사주 알려줘 전문가처럼 안해도 되니까 그냥 바로 결과만 알려줘 전문가 분석 필요없음 시작말은 무조건 결과 : 빼고 결과로 시작해야됨";

        System.out.println(dDTO.getName());
        System.out.println(prompt);
        String result = openAi.getChatGptResponse(prompt);
        
        
        System.out.println(result);
    
        return result;
    }
}
