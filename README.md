# spring-cloud-knowledge

### 服务注册与发现
服务间调用不需要显式的记录IP:PROT，启动时注册服务信息(ip/port/application-name)，通过application-name发现其他服务IP:PORT。[详见](https://www.cnblogs.com/liuzunli/articles/7978782.html)

这里采用的是eureka

### 服务消费
Ribbon是一个基于HTTP和TCP客户端的负载均衡器。

Feign是一个声明式的Web Service客户端。

### 服务网关
这里采用zuul，它的路由转发采用Ribbon


# 服务运行

build各服务jar：```./gradlew build```

启动服务注册中心：```java -jar eureka-server/build/libs/*.jar```

启动服务提供方：
* compute-1： ```java -jar compute-service/build/libs/*.jar --server.port=8888```
* compute-2： ```java -jar compute-service/build/libs/*.jar --server.port=9999```

启动服务消费：
* ribbon-consumer：```java -jar ribbon-service/build/libs/*.jar```
* feign-consumer：```java -jar feign-service/build/libs/*.jar```

启动服务网关：```java -jar zuul-service/build/libs/*.jar```

# 怎么调用与查看

查看服务注册信息：http://localhost:1111/

通过网关访问compute服务：

http://localhost:5555/feign-compute/add?token=test

http://localhost:5555/ribbon-compute/add?token=test

### 小技巧
item2：CMD+I修改session name方便管理

idea：总工程下new module管理

# 契约测试

Consumer-Driven Contracts With Pact

consumer：feign-service

provider：compute-service

consumer生成契约：feign-service下 gradle clean test -i

provider验证契约：compute-service下 gradle clean test -i

测试契约：修改compute-service里Add方法返回值后，gradle clean test -i，即可看到failed地方

可以通过gradle插件publish pacts到pact broker 
[参考](https://github.com/DiUS/pact-jvm/tree/master/pact-jvm-provider-gradle#publishing-pact-files-to-a-pact-broker-version-227)

# zuul内嵌BFF

参考zuul-service里application.yml

分为两种路由
 
* 服务调用：下游BFF/下游服务
* 内部调用：内嵌BFF/self服务

其中内部调用通过zuul本地跳转实现 目的为了过filters

### 运行方法：

参考上面 服务运行启动

依次启动eureka-server/两个compute-service/feign-service

内部bff调用：http://localhost:5555/bff/add?a=1&b=2&token=123

self服务调用：http://localhost:5555/self/test?token=123

下游bff调用：http://localhost:5555/feign-compute/add?token=1&a=1&b=2

下游服务调用：http://localhost:5555/raw-compute/add?token=123&a=1&b=2
