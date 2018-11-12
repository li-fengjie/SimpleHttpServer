# 手机服务器微架构设计与实现 之 http server



------

**本项目主要实现了：**

* **手机http server 微架构**

* **app内置网页功能**

* **WIFI传图功能**

------

  

## 一、应用场景 ##

![img](https://images2017.cnblogs.com/blog/1068222/201712/1068222-20171230204650882-2015211077.png)

 



![img](https://images2017.cnblogs.com/blog/1068222/201712/1068222-20171230205024132-203208357.png)

 

## 二、传输协议和应用层协议概念

 

**![img](https://images2017.cnblogs.com/blog/1068222/201712/1068222-20171230205934804-429638545.png)**

 

## 三、TCP ## 

**![img](https://images2017.cnblogs.com/blog/1068222/201712/1068222-20171230210813445-217446769.png)

## 四、UDP ##

**![img](https://images2017.cnblogs.com/blog/1068222/201712/1068222-20171230211007773-1613392323.png)**

 ## 五、TCP和UDP选择 ##

![img](https://images2017.cnblogs.com/blog/1068222/201712/1068222-20171230211139726-1891330627.png)

**三次握手（客户端与服务器端建立连接）/四次挥手（断开连接）过程图：**

![img](https://images2017.cnblogs.com/blog/1068222/201712/1068222-20171230210602351-1194906459.png)

 

 

## 六、java Socket 基础

 ![img](https://images2017.cnblogs.com/blog/1068222/201712/1068222-20171230211343773-1717861158.png)

 

## 七、使用Server socket创建TCP服务器端

![img](https://images2017.cnblogs.com/blog/1068222/201712/1068222-20171230211617585-1123948763.png)

 

## 八、Get 与 Post 协议格式

 ![img](https://images2017.cnblogs.com/blog/1068222/201712/1068222-20171230214839945-1524326846.png)

![img](https://images2017.cnblogs.com/blog/1068222/201712/1068222-20171230214935710-859639603.png)

## 九、开发真机与模拟器网络调试工具与配置

* **真机：开发机和真机处于同一网段下即可**
* **模拟器：**

![img](https://images2017.cnblogs.com/blog/1068222/201712/1068222-20171230212014445-1866373817.png)

注：[Win10正式版telnet不是内部或外部命令怎么办](https://jingyan.baidu.com/article/1e5468f9033a71484961b7d7.html)
