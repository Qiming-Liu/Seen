# Seen

[English](https://github.com/Qiming-Liu/Seen/blob/master/README.md) | [中文](https://github.com/Qiming-Liu/Seen/blob/master/README_CN.md)

## 1. Introduction
================ Click the picture below to play the app demo video ============  
[![Click to play](https://images.gitee.com/uploads/images/2019/0213/163441_2c9a9506_1320722.png "start.png")](https://www.youtube.com/watch?v=_2h4TFJoBZc)  

## 2. Project deployment tutorial
1. The project is divided into two parts: client and server, which need to be compiled and deployed separately  
2. Before the configure, ensure that you have known the basic operations of JDK, Android Studio, SQL Server 2008 R2, Tomcat and Server  
3. If you still need help, you can contact me by email or ask questions by Github issue  


### 2.1 Server
#### 2.1.1 Prerequisites
1. Eclipse  
2. A Windows server with public IP  
3. Install SQL Server 2008 R2 as the server database to your server  
4. Install Tomcat to your server  

#### 2.1.2 Code compilation
1. Use Eclipse to import the JavaWeb code of Server/JSP  
2. Modify the database configuration at "WEB-INF\classes\util\DBHelper.java" to make it connect to your database  
3. Compile the project as a '.war' file  

#### 3.1.3 Service Deployment
Use Tomcat load the .war file  

### 3.2 Client
#### 3.2.1 Prerequisites
Android Studio  

#### 3.2.2 Code compilation
1. Use Android Studio to import Client/Seen's Android project  
Client/Seen V0.5.3 (Not include IM) does not include instant messaging (IM)  
Client/Seen V1.0.4 (Include IM) includes all functions  
2. Modify "com\a8plus1\seen\Bean\NetData.java", change the IP address to the public IP address of your server, and make the network request your server  
3. If you use the Include IM project, you need to configure IM, check 3.3 Rongyun IM for details, if you are not using IM, skip this step  
4. Compile the Android project    

### 3.3 Rongyun IM Operation steps
1. [Apply for Rongyun Account](https://www.rongcloud.cn/)  
2. Apply for the IM function for free and get App Key, App Secret and Token  
3. Check the Rongyun document, and replace the App Key, App Secret and Token that you have applied for  
