package com.woowa.supp.web;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.woowa.supp.config.auth.LoginUser;
import com.woowa.supp.config.auth.dto.SessionUser;
import com.woowa.supp.domain.surveyee.DeveloperType;
import com.woowa.supp.domain.surveyee.Surveyee;
import com.woowa.supp.service.SurveyService;
import com.woowa.supp.web.dto.DeveloperTypeSaveRequestDto;
import com.woowa.supp.web.dto.SurveyResultResponseDto;
import lombok.RequiredArgsConstructor;

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
    public SurveyResultResponseDto findById(@LoginUser SessionUser user) {
        Surveyee surveyee = surveyService.findByLogin(user)
                                         .orElseThrow(() -> new IllegalArgumentException(
                                             "존재하지 않는 유저 입니다. name = " + user.getName() + "id = " + user.getLogin()));
        return new SurveyResultResponseDto(surveyee);
    }

    @CrossOrigin(origins = {"https://techcourse.woowahan.com"})
    @GetMapping("/api/v1/survey-result/{login}")
    public SurveyResultResponseDto findById(@PathVariable String login) {
        Surveyee surveyee = surveyService.findByLogin(login)
                                         .orElseThrow(
                                             () -> new IllegalArgumentException("존재하지 않는 유저 입니다. id = " + login));
        return new SurveyResultResponseDto(surveyee);
    }

    @GetMapping("/api/v1/result")
    public DeveloperType result(@LoginUser SessionUser user) {
        Surveyee surveyee = surveyService.findByLogin(user).orElseThrow(NoSuchElementException::new);
        return surveyee.getDeveloperType();
    }
}
