## 启动
### IDEA
增加启动参数args：botToken=xxx botName=@xxx botChatId=xxx
IDEA设置路径：Program arguments添加参数
- botToken=xxx 机器人token
- botName=@xxx 机器人名称
- botChatId=xxx tg群聊chatId
> com.jeizas.infrastructure.config.BotAutoConfiguration TG连接网络代理，按自己环境配置，后期可配置化

### docker

docker build -t demo:latest . && docker run -p 9090:8080 --env botName=name1 --env botToken=token2 --env botChatId=chatId --name demo-dev demo:latest

## TODO 待续


