# 已阅-Seen

### 项目介绍
本项目为论坛BBS、贴吧、即时通讯、Android APP、安卓应用  
包含完整开发文档、界面设计、设计说明书、科研总结、后台服务器源代码等文件  
可以快速部署运行项目，适合安卓初学者参考设计  

### 项目定位
![welcome](https://images.gitee.com/uploads/images/2019/0213/155450_9b9d05be_1320722.png "welcome.png")  
本项目定位是校内聊天论坛工具。  
在APP中每一个人都是一个“皇帝”，  
每一个帖子可以看做是奏折。  
增加了本APP的形象性与趣味性。  
这也是我们的创新点与创意点。  
是APP的灵魂所在。  

### 服务器部署
1.在Windows服务器上安装SQL Server2008作为服务器数据库  
2.安装完成后导入数据库文件"服务器\Database\Seen.sql"  
3.使用Eclipse导入"服务器\JSP\"文件夹下的名为Seen的JavaWeb项目  
4.修改"WEB-INF\classes\util\DBHelper.java"的服务器配置代码，使其与数据库连接正常  
5.编译项目为.war文件  
5.安装Tomcat并运行.war文件  
至此服务器部署完毕  

### ApK编译
在"代码"文件夹下包含两部分代码  
中期代码不包含即时通讯的功能  
结题代码包含即时通讯的功能  
请自行选择编译  
即时通讯功能使用融云SDK  
1.安装Android Studio  
2.导入中期或结题项目代码  
3.修改"com\a8plus1\seen\Bean\NetData.java"的服务器ip配置代码，使其网络请求你的服务器  
4.编译apk文件  

[![点击播放](https://images.gitee.com/uploads/images/2019/0213/163441_2c9a9506_1320722.png "start.png")](https://www.bilibili.com/video/av43421459/)  

作者 by ProSS  
