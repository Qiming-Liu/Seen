# 已阅-Seen

## 1.项目介绍
================点击下面的图片播放app演示视频============  
[![点击播放](https://images.gitee.com/uploads/images/2019/0213/163441_2c9a9506_1320722.png "start.png")](https://www.youtube.com/watch?v=_2h4TFJoBZc)   

## 2.项目定位
![welcome](https://images.gitee.com/uploads/images/2019/0213/155450_9b9d05be_1320722.png "welcome.png")  
本项目定位是校内聊天论坛工具。  
在APP中每一个人都是一个“皇帝”，  
每一个帖子可以看做是奏折。  
增加了本APP的形象性与趣味性。  
这也是我们的创新点与创意点。  
是APP的灵魂所在。  
此项目包含开发文档、界面设计、设计说明书、科研总结等。[点我跳转](https://gitee.com/pross/seen)  

## 3.项目部署教程
1. 项目分为**客户端**和**服务端**两部分,需要**分别编译部署**  
2. 配置部署过程，默认你已掌握JDK，Android Studio，Tomcat和服务器的基本操作  
3. 如果仍然需要帮助，可以通过邮件联系我或通过Issue提问  
---
## 3.1服务器
### 简易部署 (2020年11月22日更新)  
直接使用Node.js进行服务器的部署，简单，方便，快捷，无需数据库  
不熟悉服务器操作的同学建议直接使用此方法  
目前不支持连接数据库，不支持IM功能  
1. 下载并安装Node.js  
2. 打开/Server/Nodejs/目录，在此目录打开cmd（可以在资源管理器的文件夹路经输入cmd回车）  
3. 在cmd中运行以下代码  
```
npm install  
node app.js  
```
npm install是用来安装服务器所需要的模块  
node app.js是用来运行app.js的代码  
如果需要修改配置，可以在settings.js文件中修改 
如果你只是想简单使用，即可跳过下面的服务器部署编译部署过程  

#### 3.1.1前置要求
1.Eclipse  
2.一台具有公网IP的Windows服务器(腾讯云，阿里云等)  
3.在服务器上安装SQL Server2008作为服务器数据库  
4.在服务器上安装Tomcat  

#### 3.1.2代码编译
1.使用Eclipse导入Server/JSP的JavaWeb代码  
2.修改"WEB-INF\classes\util\DBHelper.java"的数据库配置，使其与你的数据库连接正常  
具体操作可以参考[(配置Eclipse通过JDBC连接访问SQL Server 2008 R2)](https://blog.csdn.net/weixin_39645559/article/details/79522379?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.edu_weight&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.edu_weight)  
3.编译项目为.war文件并传到服务器  

#### 3.1.3服务部署 
使用Tomcat并运行.war文件
至此服务器配置完成 

---
## 3.2客户端
### 简易部署 (2020年11月22日更新)  
下载V0.5.3 Release版本的apk并安装在Android Studio自带模拟器上运行   
如果你只是想简单使用，即可跳过下面的客户端部署编译部署过程  

#### 3.2.1前置要求
Android Studio (推荐，其他版本有可能打不开项目)  

#### 3.2.2代码编译
1. 使用Android Studio导入Client/Seen的Android工程  
Client/Seen V0.5.3 (Not include IM)/Seen 不包含即时通讯聊天(IM)，只包含论坛的功能，推荐此版本，无需更多配置    
Client/Seen V1.0.4 (Include IM)/Seen 包含全部功能，需要配置融云    
2. 修改"com\a8plus1\seen\Bean\NetData.java",将IP地址改为你的服务器的公网IP地址，使其网络请求你的服务器  
3. 如果你使用了Include IM的工程，则需要对IM进行配置，具体查看3.3融云IM，如果没有使用IM，跳过这步  
4. 编译apk文件  
至此客户端配置完成，可以测试使用啦  

### 3.3融云IM
融云IM提供了完善的即时通讯的功能，并且提供免费试用，其功能强大，配置简单，足够我们使用

#### 3.3.1操作步骤
代码中已经写好和融云的各种配置代码，只需按照下面的步骤将App Key,App Secret和Token替换为你自己的(我的融云账号已停止使用)  
1. [申请融云账号](https://www.rongcloud.cn/)  
2. 申请免费试用的IM功能，取得App Key,App Secret和Token  
3. 查看融云文档，将替换你申请得到的App Key,App Secret和Token  
