# search

> SpringBoot 定期抓取豆瓣租房信息推送到邮箱

刚毕业的租房狗每次找房都需要耗费大量精力,或是心仪的房子又因为信息获取不及时被他人抢走很难受

于是便有了写个小工具帮忙的想法,花了一下午加一晚上的时间完成 希望能帮助到更多在外漂泊的租房人士😆

当然 如果你刚好懂点HTML的话也可以轻松改造成其他网站信息抓取工具

> 安装步骤:

  1.application.yml文件修改邮箱发件人\邮箱授权码\收件人\检索关键词\数据库账号
  2.上传jar包 Dockerfile rebuild.sh 
  3.执行sh rebuild.sh 脚本一键运行

涉及技术: SpringBoot+SpringTask定时任务+SimpleMailMessage邮件发送



> 效果图:

<img src="https://as-note.oss-cn-shenzhen.aliyuncs.com/uploadFile/U20191108441386583/WechatIMG1.png">


> 更新日志:

2019-12-03 上传项目

2021-06-01 更新豆瓣请求头验证修复403错误
             新增数据库连接 支持多账户发送