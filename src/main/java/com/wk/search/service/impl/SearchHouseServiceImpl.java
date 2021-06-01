package com.wk.search.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.*;
import com.wk.search.mapper.SearchMapper;
import com.wk.search.model.SearchEntity;
import com.wk.search.service.SearchHouseService;
import com.wk.search.utils.MailSenderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SearchHouseServiceImpl implements SearchHouseService {
    @Resource
    private MailSenderUtil mailSenderUtil;
    @Resource
    private SearchMapper searchMapper;
    @Value("${spring.search.searchUrls}")
    private String searchUrls;

    static InputStream inputStream;

    @Override
    public void search() throws IOException {
        List<SearchEntity> list = searchMapper.getSearchInfo();
        for (SearchEntity entity : list) {
            send(entity);
        }
    }

    @Override
    public String switchStatus(String email) {
        Integer status = searchMapper.statusByEmail(email);
        if (status != null) {
            if (status == 0) {
                searchMapper.switchStatus(email, 1);
                return "已开启提醒";
            }
            if (status == 1) {
                searchMapper.switchStatus(email, 0);
                return "已关闭提醒";
            }
        }
        return "收件人不存在";
    }

    private String[] getLines(String searchUrl, String keyword) throws Exception {
        // 配置Header
        Header[] headers = HttpHeader.custom()
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:88.0) Gecko/20100101 Firefox/88.0")
                .accept("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .connection("keep-alive")
                .cookie("douban-fav-remind=1; gr_user_id=0942ea2f-0017-432a-9066-d3c932cbf4d3; _vwo_uuid_v2=D65E3291E2809E15DABB76F162E79B78E|7510a95ec70229c82ba0d0950809cc7a; douban-profile-remind=1; __utma=30149280.693633016.1580883852.1622510990.1622517315.87; __utmz=30149280.1622517315.87.62.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; _pk_id.100001.8cb4=48a0edbb66ec2762.1582527590.53.1622517332.1622514273.; __utmv=30149280.7079; _pk_ref.100001.8cb4=%5B%22%22%2C%22%22%2C1622517314%2C%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DfpFiZ1RT_nYnpeCA-5_HWmIusXx0QZTmqP4ZwUk1WSD_SiUGkZqfJMJlBJMkWl6R%26wd%3D%26eqid%3Ddbe20b1b00002a260000000660b5a63e%22%5D; viewed=\"34819650_20432061_1088054_26282806_35252621_30221096_27179953_30417623_4848587_26740520\"; bid=djiWilf_lI4; ll=\"118282\"; dbcl2=\"70797010:ru2VPoAi+jE\"; push_noty_num=0; push_doumail_num=0; ck=_TY-; __yadk_uid=cWUBOaWz2pt5Bu5vr9pSoXHeiBwrjXf3; __gads=ID=8f15d2ec8393748a-22899e8113c90073:T=1622424165:RT=1622424165:S=ALNI_MavpFd32m-b6CCe1JMBfAaWKW5AnQ; __utmc=30149280; ap_v=0,6.0; _pk_ses.100001.8cb4=*; __utmb=30149280.6.10.1622517315; __utmt=1")
                .build();
        HttpConfig config = HttpConfig.custom()
                .headers(headers)
                .timeout(3000)
                .url(searchUrl + keyword)
                .context(HttpCookies.custom().getContext());
        HttpResult respResult = HttpClientUtil.sendAndGetResp(config);
        String result = respResult.getResult();
        if (result.contains("403 Forbidden")) {
            log.info("403 Forbidden");
        }
        //获取网页源码
        return result.split("\\r?\\n");
    }

    private void send(SearchEntity searchInfo) throws IOException {
        Float scope = searchInfo.getScope();
        String receiver = searchInfo.getReceiver();
        String[] keywords = searchInfo.getKeywordsList().split(",");
        String[] blackWords = searchInfo.getBlackWordsList().split(",");
        StringBuilder validText = new StringBuilder();
        StringBuilder infoStr = new StringBuilder();
        String[] urlList = searchUrls.split(",");
        int year = Calendar.getInstance().get(Calendar.YEAR);
        try {
            for (String keyword : keywords) {
                for (String searchUrl : urlList) {
                    String[] lines = getLines(searchUrl, keyword);
                    //逐行读取网页源码
                    for (String line : lines) {
                        //找到有效数据
                        if (line.contains("group_topic_by_time") || (line.contains("\n") && line.endsWith("</a></td>"))) {
                            validText = new StringBuilder(line);
                        }
                        if (line.contains("td-time") && (line.contains(year + "") || line.contains(year - 1 + ""))) {
                            validText.append(line);
                            int startIndex = validText.indexOf(year + "-");
                            String dateStr = validText.substring(startIndex, validText.indexOf("\" nowrap="));
                            String urlStr = validText.substring(validText.indexOf("http"), validText.indexOf("\" onclick"));
                            String titleStr = validText.substring(validText.indexOf("title=\"") + 7, validText.lastIndexOf("<td class=\"td-time\""));
                            if (titleStr.contains("</a></td>")) {
                                titleStr = titleStr.substring(0, titleStr.indexOf("</a></td>"));
                            }
                            if (titleStr.contains("\">")) {
                                titleStr = titleStr.substring(0, titleStr.indexOf("\">"));
                            }
                            String info = "[" + keyword + "]\r\n" + "标题：" + titleStr + "\r\n" + dateStr + "\r\n" + urlStr + "\r\n";
                            Date parse = DateUtil.parse(dateStr, "yyyy-MM-dd HH:mm:ss");
                            float time = (new Date().getTime() - parse.getTime()) / 1000F / 60F / 60F;
                            boolean blackWordFlag = true;
                            for (String blackWord : blackWords) {
                                if (!StrUtil.isEmpty(blackWord) && titleStr.contains(blackWord)) {
                                    blackWordFlag = false;
                                    break;
                                }
                            }
                            if (blackWordFlag && time < scope
                                    && !infoStr.toString().contains(titleStr.trim())
                                    && ((titleStr.length() <= 9) || !(infoStr.toString().contains(titleStr.substring(0, 8))))
                                    && ((titleStr.length() <= 6) || !(infoStr.toString().contains(titleStr.substring(titleStr.length() - 5, titleStr.length() - 1))))
                                    && !titleStr.contains("求租")
                                    && (titleStr.contains("房") || titleStr.contains("租") || titleStr.contains("间") || titleStr.contains("厅")
                                    || titleStr.contains("室") || titleStr.contains("住"))) {
                                //如果数据符合上述条件 拼接字符串
                                infoStr.append(info).append("\r\n");
                            }
                        }
                    }
                }
            }
            // log.info("\r\n" + "[最终正文]:" + "\r\n" + infoStr);
            if (infoStr.length() > 0) {
                String str = "====================================" + "\r\n" + "检索范围: " + scope + "小时内" + "\r\n" + "关键词: " + Arrays.toString(keywords) + "\r\n" + "====================================" + "\r\n";
                mailSenderUtil.sendSimpleMail(receiver, "豆瓣租房-发现新房源", str + infoStr.toString());
                log.info("{} 邮件发送成功!", receiver);
            } else {
                log.info("未找到合适信息");
            }
        } catch (Exception e) {
            log.info("邮件发送失败!");
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                inputStream.close();
        }
    }
}
