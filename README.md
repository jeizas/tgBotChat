## 启动
### IDEA本地启动
1. 需要准备参数：增加启动参数args：botToken=xxx botName=@xxx botChatId=xxx 
2. IDEA设置路径：Program arguments添加参数
3. 参数说明
- botToken=xxx 机器人token
- botName=@xxx 机器人名称
- botChatId=xxx tg群聊chatId
4. 如果本地网络需要设置http代理才能连接TG，则需要如下代码，并设置启动

``` java
// 类：com.jeizas.infrastructure.config.BotAutoConfiguration
botOptions.setProxyHost("127.0.0.1");
botOptions.setProxyPort(8888);

// 修改application.yml
env=local
```

### docker启动
- 构建
``` shell
mvn clean install -Dmaven.test.skip=true
docker build -t demo:latest .
```
- 部署
``` shell
docker run -p 8080:8080 --env botName='botName=xxx' --env botToken='botToken=xxx' --env botChatId='botChatId=xxx' --name demo-dev demo:latest
```