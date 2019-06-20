# Java微服务新生代之Nacos
![公众号](http://ww4.sinaimg.cn/large/006tNc79ly1g433etg203j30p00dw40e.jpg)

## 前言

从 2017 年底 Java 开发领域使用最广的 RPC 框架 Dubbo 开启重新更新维护之路开始，阿里巴巴为打造 Dubbo 微服务生态持续开源了 [Sentinel](https://github.com/alibaba/Sentinel)，[Nacos](https://github.com/alibaba/Nacos)，[Seata](https://github.com/seata/seata) 等微服务中间件框架，并且推出了 Spring Cloud Alibaba 来提供微服务开发的一站式解决方案，阿里巴巴在 Java 社区持续活跃起来，也为 Java 微服务开发注入了新的活力。

本篇文章将重点学习微服务组件 Nacos 作为注册中心的功能和用法，**Nacos** 是阿里巴巴于 2018 年 7 月份新开源的项目。

> 关于 Nacos 名字：前四个字母分别为 Naming 和 Configuration 的前两个字母，最后的 `s` 为Service。

本文主要内容涉及如下：

- Nacos 基本介绍
- 为什么使用 Nacos
- 最新版本 Nacos 与 Rest/Dubbo 服务的注册与发现集成

## 关于 Nacos /nɑ:kəʊs/

> 一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。

上面这句话摘自 Nacos 官方首页，是对 Nacos 整体功能的总结。

简单来说 Nacos 就是注册中心 + 配置中心的组合，提供简单易用的特性集，帮助我们解决微服务开发必会涉及到的服务注册与发现，服务配置，服务管理等问题。Nacos 还是 Spring Cloud Alibaba 组件之一，负责**服务注册与发现**。

首先来看下官方对 Nacos 的[特性](https://nacos.io/zh-cn/docs/what-is-nacos.html)描述：

- **服务发现和服务健康监测**
- **动态配置服务**
- **动态 DNS 服务**
- **服务及其元数据管理**
- **不断新增...**

可以发现所有特性都离不开**服务**这一词，服务 (Service) 是 Nacos 世界中的一等公民，说明了服务是 Nacos 中最主要的角色。

## 为什么是 Nacos

现在的微服务生态中，已经有很多服务注册与发现的开源组件，如 Eurka，ZooKeeper，Consul，为什么还要用 Nacos 呢，我们看下这些框架的简单对比：

| 服务注册与发现框架 | CAP 模型 | 控制台管理 | 社区活跃度        |
| ------------------ | :------- | :--------- | ----------------- |
| Eureka             | AP       | 支持       | 低 (2.x 版本闭源) |
| Zookeeper          | CP       | 不支持     | 中                |
| Consul             | CP       | 支持       | 高                |
| Nacos              | AP       | 支持       | 高                |

> 据说 Nacos 在阿里巴巴内部有超过 10 万的实例运行，已经过了类似双十一等各种大型流量的考验。

相比之下，目前的 Nacos 无论是部署，还是使用上都简单上手，更重要的是文档资料齐全，社区活跃度高。

并且 Nacos 与目前主流的开源生态都提供了很好的支持：

- Nacos 是专为 Dubbo 而生的注册中心与配置中心
- Nacos 会完全兼容 Spring Cloud
- Nacos 支持 Service Mesh 集成，Kubernetes 集成

除此之外，阿里巴巴正在通过 Dubbo + Nacos 以及一系列开源项目打造服务发现、服务及流量管理、服务共享平台，未来还在不断地发展和演进,相信未来会有更多的地方上使用 Nacos。

## Nacos 实战

### 单机部署

#### 准备环境

Nacos 依赖 Java 环境来运行，并且需要对 Nacos 代码构建生成可执行程序时，还要有 Maven 环境，所以部署前需要保证环境要求：

- 64 bit OS，支持 Linux/Unix/Mac/Windows，推荐选用 Linux/Unix/Mac。
- 64 bit JDK 1.8+
- Maven 3.2.x+

#### 下载安装

![下载](http://ww2.sinaimg.cn/large/006tNc79ly1g42a1glny7j311s096q3m.jpg)

当前最新的 Nacos 版本为 1.0.1，Maven 方式打包后会在当前目录 `distribution/target` 下生成两个压缩包 `nacos-server-1.0.1.tar.gz` 和 `nacos-server-1.0.1.zip`，任意解压一个使用即可。

#### 解压运行

![解压](http://ww2.sinaimg.cn/large/006tNc79ly1g42abquylvj311s076jrz.jpg)

这里 Nacos 单机部署方式使用命令 `-m standalone` ，如果是 Windows 命令，可以直接双击 `startup.cmd` 文件即可。

![运行](http://ww3.sinaimg.cn/large/006tNc79ly1g42ahvf45rj312d0u0q7e.jpg)

当控制台出现 Nacos 字母，并且出现 `Nacos started successfully in stand alone mode` 提示时就说明了 Nacos 服务端启动成功，控制台上也直接给出了当前可访问的 Nacos 控制台地址 `http://{ip}:8848/nacos/index.html`，点击就进入了 Nacos 的可视化管理界面，需要账号密码登录访问，默认都为 `nacos`。

![image-20190616001254325](http://ww4.sinaimg.cn/large/006tNc79ly1g42amshhoej31fk0kg0v3.jpg)

登录之后就能在网站侧边栏上看到 Nacos 的主要功能菜单：配置管理，服务管理，集群管理，命名空间。对于服务注册与发现功能来说，我们只要看关注服务管理即可。

![image-20190616001603599](http://ww2.sinaimg.cn/large/006tNc79ly1g42aq2dhhgj30620cldfu.jpg)

服务管理下目前只有一个名为服务列表的子菜单，展示的内容也比较简单，包含服务名称，组别，集群数目，总的实例数，运行中的实例数。

![image-20190616001806599](http://ww2.sinaimg.cn/large/006tNc79ly1g42as76454j31aw0a674q.jpg)

当有新的服务通过 Nacos 客户端注册到 Nacos 上时列表项就会增加，我们还可以通过操作栏的按钮进行对服务的详情查看和编辑。

### 集群部署

相比 Nacos 简单的单机部署，集群部署方式稍微麻烦一些，跟着官方文档走还是有点小坑，还需要自己额外的调整。 为了用于生产环境，必须确保 Nacos 的高可用，所以还是有必要实践下集群部署的操作。

准备环境跟单机部署相同，额外的要求就是 Nacos 需要 3 个或 3 个以上 Nacos 节点构成集群，并且使用 MySQL 作为数据源，主要用于服务配置的数据持久化。

我们先看下官方推荐的集群部署架构图，通过域名方式反向代理如 Nginx 来负载多个 Nacos 节点 IP，外部客户端直接通过域名访问就可，不仅可读性好，而且更换 IP 方便，最为推荐采用。

![deployDnsVipMode.jpg](http://ww3.sinaimg.cn/large/006tNc79ly1g42d2b6waej30ui0d8q47.jpg)

#### 添加集群配置文件

在每个 Nacos 节点的`conf`目录下，添加配置文件 `cluster.conf`，可以参考相同目录下的 `cluster.conf.example` 文件，每行配置一个节点的 IP 和端口，如 `ip:port`

![](http://ww2.sinaimg.cn/large/006tNc79ly1g42d9j1c1lj311s06o3yq.jpg)

> 注意：配置文件中不能使用 `127.0.0.1` 或者`localhost` ，需要真实 IP 或者域名，否则启动后服务无法注册到该集群节点上, 详见[NACOS ISSUE #1189](https://github.com/alibaba/nacos/issues/1189) 。

#### 配置 MySQL 数据库

Nacos 推荐生产环境中数据库使用建议至少主备模式，或者采用高可用数据库。

这里为了简化只采用了一个数据库。首先新建一个名为 `nacos_config` 的数据库，使用提供的 [sql 语句源文件](https://github.com/alibaba/nacos/blob/master/distribution/conf/nacos-mysql.sql) 导入初始数据。

![image-20190616003609656](http://ww1.sinaimg.cn/large/006tNc79ly1g42cx4wbxxj30aj08djrl.jpg)

然后在每个 Nacos 节点的配置文件 `conf/application.properties` 里添加数据库连接配置：

![application](http://ww2.sinaimg.cn/large/006tNc79ly1g42cz88hwej314s0auq46.jpg)

最后以集群模式分别启动每个节点，并且默认为后台启动，启动信息需要从 `logs/logs/start.out`日志文件中获取。

![start.out](http://ww1.sinaimg.cn/large/006tNc79ly1g42d128azqj311s05a74g.jpg)

当日志文件最后出现 `Nacos started successfully in cluster mode.` 一行时，即说明集群模式下 Nacos 启动成功。这时，我们也可以通过登录任一个 Nacos 控制台的集群管理界面看到节点的信息。

![image-20190616015114761](http://ww1.sinaimg.cn/large/006tNc79ly1g42dh41khqj31eh0bp3z8.jpg)

可以从上面看到，集群下的 Nacos 节点状态分为 `FOLLOWER` ，`LEADER` 两种，跟我们熟悉的主从架构相似。

到这里，我们集群方式的搭建也完成了。接下我们就来看下如何使用 Nacos 进行服务注册和发现吧。

### 实现服务的注册与发现

#### Rest 服务的注册与发现

##### 服务提供者创建

创建一个子项目工程名为 `rest-provider` 的服务提供者项目，`pom.xml` 配置如下：

![rest-provider](http://ww4.sinaimg.cn/large/006tNc79ly1g42ssqhjbtj30u010otlc.jpg)

注意这里的 `spring-cloud-starter-alibaba-nacos-discovery` 版本为 0.9.0，采用的是 Nacos 1.0.0 版本的客户端，而对应 Spring Boot 版本需要为 2.1.x.RELEASE 版本， 更多版本对应关系参考：[版本说明 Wiki](https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/版本说明)

![image-20190616105233354](http://ww2.sinaimg.cn/large/006tNc79ly1g42t4bm4i8j30iv07oaak.jpg)

因此我们使用 Spring Cloud Alibaba 最新版本 `0.9.0.RELEASE`,对应 Spring Cloud Greenwich 版本，父 Maven 项目 POM 配置需要添加如下依赖管理：

![pom](http://ww4.sinaimg.cn/large/006tNc79ly1g431auk6fmj31050u0td2.jpg)

依赖添加之后，在引导类中实现一个 Rest 请求方法 `/echo`，并且通过 `@EnableDiscoveryClient` 注解表明是一个 Nacos 客户端，而该注解是 Spring Cloud 提供的原生注解，就算切换成 Eureka 作为注册中心也是用这个注解。

![引导类](http://ww3.sinaimg.cn/large/006tNc79ly1g42t6z4fqwj311s0ncjur.jpg)

修改子项目配置文件 `application.properties`

![application](http://ww4.sinaimg.cn/large/006tNc79ly1g42tbc4rmrj311s06o3zf.jpg)

- `spring.application.name` 声明了服务名称
- `spring.cloud.nacos.discovery.server-addr` 指定了 Nacos 注册中心地址

> Nacos Starter 更多配置项信息可以参考 [Spring Cloud Alibaba Nacos Discovery](https://github.com/spring-cloud-incubator/spring-cloud-alibaba/wiki/Nacos-discovery)

然后通过引导类 `RestProviderApplication` 运行项目，登录 Nacos Server 控制台，查看服务列表即可看到服务提供者 `rest-provider` 已经注册成功。

![image-20190616110759521](http://ww3.sinaimg.cn/large/006tNc79ly1g42tkdwskcj31gg0bf3z3.jpg)

##### 服务消费者创建

同样的方式我们创建一个服务消费者项目 `rest-consumer`，`pom.xml` 依赖和 `application.properties` 配置与 `provider-consumer` 一样, 这里我们显式地使用 LoadBalanceClient API 和 RestTemplate 结合的方式来消费服务。

1. 首先在引导类中添加 `@EnableDiscoveryClient` 注解![引导类](http://ww3.sinaimg.cn/large/006tNc79ly1g42two4g0aj311s0c8wfl.jpg)

2. 再创建一个名为 `RestConsumerConfiguration` 的 Java 配置类，注入 `RestTemplate`。![RestConsumerConfiguration.java](http://ww2.sinaimg.cn/large/006tNc79ly1g42tzlqm6oj311s0c8q3n.jpg)

3. 创建一个名为 `RestConsumerController` 测试用的 Controller，提供一个请求方法 `echo`， 如下：![RestConsumerController.java](http://ww3.sinaimg.cn/large/006tNc79ly1g42uac32rzj31am0u0jux.jpg)

4. 启动 `rest-cosumer` 工程后，就可以在 Nacos Server 控制台的服务列表里看到多了一个名为 `rest-consumer` 的服务,并且直接在浏览器访问 `http://localhost:8082/echo`，就会看到：

   ![image-20190616113423983](http://ww2.sinaimg.cn/large/006tNc79ly1g42ubzzkvej30bz03pjrd.jpg)

使用上看是不是很简单呢，其实和 Eureka 没有太大差别，除了这种方式消费 Rest 服务外，Nacos 也支持 Feign 方式，我们来看下这又是如何使用的吧。

#### Feign 方式消费 Rest 服务

首先需要引入 Feign 客户端依赖：

![Feign POM](http://ww2.sinaimg.cn/large/006tNc79ly1g42yvotbahj311s082gmg.jpg)

然后引导类添加注解 `@EnableFeignClients` 启用 Feign 组件功能，编写一个 `EchoService` 接口,用于调用远程服务：

![EchoService](http://ww3.sinaimg.cn/large/006tNc79ly1g42yyoztegj311s0aujsi.jpg)

> Get 请求方式的接口方法参数不能必须使用 `@RequestParam` 参数指定请求参数名称，否则 Feign 都会通过 POST 方式调用，得到状态码 405 的错误。

注解 `@FeignClient` 指明了调用的服务提供方名称，`echo` 方法通过 Spring MVC 提供的注解就可以跟服务提供者的 Rest 接口关联，执行时就会调用对应请求方法。

最后我们只需要编写一个注入 `EchoService` 控制器 `RestConsumerFeignController` 类，提供一个接口调用接口。

![RestConsumerFeignController](http://ww1.sinaimg.cn/large/006tNc79ly1g42z2273vnj311s0hs0ty.jpg)

同样启动后，直接打开 `http://localhost:8082/echo/hi`，就会得到浏览器如下输出：![image-20190616142413540](http://ww1.sinaimg.cn/large/006tNc79ly1g42z8kw579j309y04iaa0.jpg)

#### Dubbo 服务的注册与发现

接下来，我们看下 Dubbo 怎么使用 Nacos 作为服务注册中心，进行注册与发现的，其实只要用过 ZooKeeper 方式的就会发现很简单。

首先我们创建一个子工程 `dubbo-serivce`，POM 文件添加依赖如下：

![POM](http://ww2.sinaimg.cn/large/006tNc79ly1g42zccmnu0j30u01cbdl5.jpg)

- 这里我们按照官方推荐使用 2.6.5 版本的 Dubbo 集成 Nacos。
- `dubbo-registry-nacos` 是 Dubbo 使用 Nacos 作为注册中心的关键依赖，当前版本为 0.0.1。

接着定义服务接口 `DemoService`：

![DemoService](http://ww2.sinaimg.cn/large/006tNc79ly1g42zhdwxjoj311s06ot8y.jpg)

##### 服务生产者

还是服务生产者先来，添加一个类 `DemoServiceImpl` 实现服务接口

![DemoServiceImpl](http://ww1.sinaimg.cn/large/006tNc79ly1g42zilbu3tj31hc0c8wfq.jpg)

然后以 XML 配置方式配置 Dubbo，只需要在 `dubbo:registry`元素上配置 Nacos 服务端地址即可。

![XML](http://ww1.sinaimg.cn/large/006tNc79ly1g42zkwano5j31kw0ri7a2.jpg)

最后用简单类`BasicProvider`的 `main` 方法直接启动服务生产者，保持进程常驻。

![BasicProvider](http://ww4.sinaimg.cn/large/006tNc79ly1g42znyaltej31kw0f0myv.jpg)

正常启动后我们直接在 Nacos 控制台的服务列表里就可以，Dubbo 生产者服务已经成功注册到 Nacos 上了，可以通过详情看到服务的具体数据，比如服务端口，方法之类。

![image-20190616144317355](http://ww2.sinaimg.cn/large/006tNc79ly1g42zseuy8dj31k40qt0v7.jpg)

##### 服务消费者

有了服务生产者，就来看下怎么去通过 Nacos 调用 Dubbo 服务。

首先，通过 XML 配置方式注入用 Dubbo 服务对象 `DemoService`, 在 `dubbo:registry`元素上配置 Nacos 服务端地址。

![XML](http://ww3.sinaimg.cn/large/006tNc79ly1g42zv2feqdj31kw0ri7a2.jpg)

然后编写简单类 `BasicConsumer` ,在 `main` 方法中加载 Spring 容器，获取 `DemoService` 对象，直接进行 RCP 调用。

![BasicConsumer](http://ww1.sinaimg.cn/large/006tNc79ly1g42zyok533j31kw0ridj8.jpg)

运行之后，我们可以从 IDE 控制台上看到不断有日志输出，说明了通过 Nacos 注册中心的Dubbo 服务调用成功了。

![IDE 控制台](http://ww4.sinaimg.cn/large/006tNc79ly1g4300b3nqrj311s09gmy5.jpg)

## 结语

本文主要学习了解 Nacos，以及使用 Nacos 的服务注册与发现功能，如何与 Rest 服务，Dubbo 服务进行集成使用，整体上简单易用，有兴趣的朋友也尝试用下吧。后续我将继续深入研究 Spring Cloud Alibaba 微服务生态的组件，欢迎感兴趣的小伙伴可以关注我的微信公众号，每周一更。


## 代码示例

本文所涉及所有代码片段均在下面仓库中，欢迎感兴趣的小伙伴参考学习：

**nacos-actions**：https://github.com/wrcj12138aaa/nacos-actions

环境支持：

- JDK 8
- Spring Boot 2.1.0
- Maven 3.6.0

## 参考

- 支持 Dubbo 生态发展，阿里巴巴启动新的开源项目 Nacos：https://yq.aliyun.com/articles/604028
- Nacos 官方文档：https://nacos.io/zh-cn/docs/what-is-nacos.html
- Spring Cloud 服务发现新选择 - Alibaba Nacos Discovery：https://www.bilibili.com/video/av32191103?from=search&seid=8421504995883713886
- 集群部署说明：https://nacos.io/zh-cn/docs/cluster-mode-quick-start.html
- NACOS #1189：https://github.com/alibaba/nacos/issues/1189
- Spring Cloud 中如何使用 Feign 构造多参数的请求：https://www.jianshu.com/p/7ce46c0ebe9d
