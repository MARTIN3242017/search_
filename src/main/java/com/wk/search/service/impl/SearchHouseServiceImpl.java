package com.wk.search.service.impl;


import com.wk.search.service.SearchHouseService;
import com.wk.search.utils.MailSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
public class SearchHouseServiceImpl implements SearchHouseService {
    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Value("${spring.search.frequent}")
    private float frequent;
    @Value("${spring.search.keywordsList}")
    private String keywordsList;
    @Value("${spring.search.searchUrl}")
    private String searchUrl;
    @Value("${spring.search.receiver}")
    private String receiver;

    @Override
    public String search() {
        String[] keywords = keywordsList.split(",");
        InputStream inputStream = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        URL url;
        BufferedReader br;
        String line;
        String validText = "";
        String infoStr = "";
        try {
            for (String keyword : keywords) {
                System.out.println("keyword :" + keyword);
                url = new URL(searchUrl + keyword);
                inputStream = url.openStream();
                br = new BufferedReader(new InputStreamReader(inputStream));
                ArrayList<String> arrayList = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    if (line.contains("group_topic_by_time") || (line.contains("\n") && line.endsWith("</a></td>"))) {
                        validText = line;
                    }
                    if (line.contains("td-time")) {
                        validText = validText + line;
                        String dateStr = validText.substring(validText.indexOf("2019"), validText.indexOf("\" nowrap="));
                        String urlStr = validText.substring(validText.indexOf("http"), validText.indexOf("\" onclick"));
                        String titleStr = validText.substring(validText.indexOf("title=\"") + 7, validText.lastIndexOf("<td class=\"td-time\""));
                        if (titleStr.contains("</a></td>")) {
                            titleStr = titleStr.substring(0, titleStr.indexOf("</a></td>"));
                        }
                        if (titleStr.contains("\">")) {
                            titleStr = titleStr.substring(0, titleStr.indexOf("\">"));
                        }
                        String info = "[" + keyword + "]\r\n" + "标题：" + titleStr + "\r\n" + dateStr + "\r\n" + urlStr + "\r\n";
                        Date parse = simpleDateFormat.parse(dateStr);
                        float time = (new Date().getTime() - parse.getTime()) / 1000F / 60F / 60F;
                        if (time < frequent && !(infoStr.contains(info.substring(0, 20))) && (!info.contains("求租"))
                                && (info.contains("房") || info.contains("租") || info.contains("间") || info.contains("厅")
                                || info.contains("室") || info.contains("住"))) {
                            infoStr += info + "\r\n";
                        }
                    }
                }
            }
            System.out.println(infoStr);
            if (infoStr != null && infoStr.length() > 0) {
                mailSenderUtil.sendSimpleMail(receiver, "豆瓣租房(最近" + (int) frequent + "小时)", infoStr);
            }
            return infoStr;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ignored) {
            }
        }
        return null;
    }
}
