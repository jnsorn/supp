package com.woowa.supp.web;

import com.woowa.supp.config.auth.LoginUser;
import com.woowa.supp.config.auth.dto.SessionUser;
import com.woowa.supp.domain.surveyee.Surveyee;
import com.woowa.supp.service.SurveyService;
import com.woowa.supp.web.dto.DeveloperTypeSaveRequestDto;
import com.woowa.supp.web.dto.SurveyResultResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class SurveyApiController {

    private final SurveyService surveyService;

    @PutMapping("/api/v1/survey-type")
    public Long saveType(@RequestBody DeveloperTypeSaveRequestDto requestDto, @LoginUser SessionUser user) {
        return surveyService.findByLogin(user)
                .map(surveyee -> surveyService.updateType(requestDto, surveyee))
                .orElseGet(() -> surveyService.saveType(requestDto, user));
    }

    @PutMapping("/api/v1/survey-style")
    public Long saveStyle(@RequestBody Map<String, Object> styles, @LoginUser SessionUser user) {
        return surveyService.saveStyle(styles, user);
    }

    @GetMapping("/api/v1/survey-result")
    public SurveyResultResponseDto findById (@LoginUser SessionUser user) {
        Surveyee surveyee = surveyService.findByLogin(user)
                .orElseThrow(IllegalArgumentException::new);
        return new SurveyResultResponseDto(surveyee);
    }

    @GetMapping("/api/v1/survey-result/{login}")
    public SurveyResultResponseDto findById (@PathVariable String login) {
        Surveyee surveyee = surveyService.findByLogin(login)
                .orElseThrow(IllegalArgumentException::new); // TODO: 2020/04/18 처리 필요
        return new SurveyResultResponseDto(surveyee);
    }
}
