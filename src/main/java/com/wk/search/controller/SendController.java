package com.wk.search.controller;

import com.wk.search.service.SearchHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableScheduling
public class SendController {

    @Autowired
    private SearchHouseService searchHouseService;

    @GetMapping(value = "/send")
    @ResponseBody
    @Scheduled(cron = "0 0 22 * * ?") //每天22点执行一次
    public void sendEmail() {
        try {
            searchHouseService.search();
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
