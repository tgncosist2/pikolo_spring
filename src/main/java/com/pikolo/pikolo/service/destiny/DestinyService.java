package com.pikolo.pikolo.service.destiny;

import org.springframework.stereotype.Service;

import com.pikolo.pikolo.dto.destiny.DestinyDTO;

@Service
public class DestinyService {
    
    public String birthPrompt(String birthTime, String language){
      String birthPrompt = "";
      if(birthTime.equals("모름") || birthTime.equals("出生時間が不明")){
       
        switch (language) {
        case "kr": birthPrompt = "태어난 시각은 모르지만 태어난 시각 모르는 것을 감안해주고";
          break;
        case "jp": birthPrompt = "生まれた時間は分かりませんが、それを考慮してください。";
        break;
        case "en": birthPrompt= "I don't know the time of birth, but please take that into consideration.";
        break;
        default: birthPrompt = "태어난 시각은 모르지만 태어난 시각 모르는 것을 감안해주고";
          break;
         }  
      } else{
        birthPrompt= birthTime;
      }

        return birthPrompt;
    }

    public String makePrompt(DestinyDTO dDTO){
        String prompt = "";
        String language = dDTO.getLanguage();
        String birthPrompt = birthPrompt(dDTO.getBirthTime(), language);
    switch (language) {
        case "kr":
            prompt = "이름 : " + dDTO.getName() +
                    " 생년월일 : " + dDTO.getBirthDate() +
                    " 양력/음력(평달)/음력(윤달) : " + dDTO.getCalendar() +
                    " 성별 : " + dDTO.getGender() +
                    " 태어난시각 : " + birthPrompt +
                    " 이 정보들로 재미로보는 평생사주 총평, 초년운, 중년운, 말년운, 건강운, 이성운, 재물운, 재물모으는법 사주 알려줘 전문가처럼 안해도 되니까 그냥 바로 결과만 알려줘 전문가 분석 필요없음 시작말은 무조건 결과 : 빼고 결과로 시작해야됨";
            break;
        case "jp":
            prompt = "名前 : " + dDTO.getName() +
                    " 生年月日 : " + dDTO.getBirthDate() +
                    " 太陰暦/太陽暦 : " + dDTO.getCalendar() +
                    " 性別 : " + dDTO.getGender() +
                    " 出生時間 : " + birthPrompt +
                    " この情報をもとに、総合運、青年運、中年運、晩年運、健康運、恋愛運、金運、お金を貯める方法などを教えてください。専門家のようにする必要はありません。結果から始めてください。";
            break;
        case "en":
            prompt = "Name: " + dDTO.getName() +
                    " Date of Birth: " + dDTO.getBirthDate() +
                    " Calendar Type: " + dDTO.getCalendar() +
                    " Gender: " + dDTO.getGender() +
                    " Time of Birth: " + birthPrompt +
                    " Based on this information, give me a fun lifelong fortune summary, youth fortune, middle age fortune, old age fortune, health, love, money luck, and tips for accumulating wealth. No need to be professional, just start directly with the result.";
            break;
        default:
            // 한국어를 기본값으로
            prompt = "이름 : " + dDTO.getName() +
                    " 생년월일 : " + dDTO.getBirthDate() +
                    " 양력/음력(평달)/음력(윤달) : " + dDTO.getCalendar() +
                    " 성별 : " + dDTO.getGender() +
                    " 태어난시각 : " + birthPrompt +
                    " 이 정보들로 재미로보는 평생사주 총평, 초년운, 중년운, 말년운, 건강운, 이성운, 재물운, 재물모으는법 사주 알려줘 전문가처럼 안해도 되니까 그냥 바로 결과만 알려줘 전문가 분석 필요없음 시작말은 무조건 결과 : 빼고 결과로 시작해야됨";
            break;
    }

    return prompt;
    }


}
