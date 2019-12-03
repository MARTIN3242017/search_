# search
SpringBoot 定期抓取豆瓣租房信息推送到邮箱

刚毕业的租房狗每次找房都需要耗费大量精力,或是心仪的房子又因为信息获取不及时被他人抢走很难受

于是便有了写个小工具帮忙的想法,花了一下午加一晚上的时间完成 希望能帮助到更多在外漂泊的租房人士😆

当然 如果你刚好懂点HTML的话也可以轻松改造成其他网站信息抓取工具

安装步骤:

  1.application.yml文件修改邮箱发件人\邮箱授权码\收件人\检索关键词

  2.java -jar search.jar 一键运行 无需其他依赖

涉及技术: SpringBoot+SpringTask定时任务+SimpleMailMessage邮件发送
