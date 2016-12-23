# simple-websocket-server
java netty实现的简单websocket服务器,支持二进制
# 根据RFC 6455文档开发
所有编解码器都是自己实现，包括http编解码，websocket升级握手，websocket frame编解码
# 服务器
运行MyWebsocktServer.java中的main方法即可启动
# 客户端
打开test.html网页，内有文本类型帧和二进制帧的演示demo