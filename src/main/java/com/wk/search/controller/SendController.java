package com.wk.search.controller;

import com.wk.search.service.SearchHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@EnableScheduling
public class SendController {

    @Autowired
    private SearchHouseService searchHouseService;

    @GetMapping(value = "/send")
    @ResponseBody
    @Scheduled(cron = "0 0 0/1 * * ?") //每小时执行一次
    public String sendEmail() {
        try {
            searchHouseService.search();
            return "请求成功!";
        } catch (MailException | IOException ex) {
            System.err.println(ex.getMessage());
            return "请求失败!";
        }
    }

    @GetMapping(value = "/switch")
    public String sendEmail(@RequestParam String email) {
        return searchHouseService.switchStatus(email);
    }
}
