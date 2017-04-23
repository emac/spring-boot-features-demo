# Spring Boot 1.x版本特性展示项目

## 简介

[Spring Boot](https://projects.spring.io/spring-boot/)是Spring开源社区提供的一个去容器、去XML配置的应用框架。和标准的基于war包的Web应用相比，Spring Boot应用可以直接以`java -jar`的方式运行，也就是说不再需要部署到一个独立的Web容器（比如Tomcat）中才能运行。其背后的运行机制简单来说就是，当一个Spring Boot应用启动时，在加载完核心框架类之后，会启动一个内嵌的Web容器（默认是Tomcat），然后再加载应用本身的各种配置类和Bean。也就是说不再是容器包应用，而是应用包容器。

![](https://s3.amazonaws.com/media-p.slid.es/uploads/40667/images/2863143/QQ20160722-0_2x.png)

以下是目前本工程展示的所有Spring Boot特性列表：

- Spring框架
  - Thymeleaf集成（含分页）
  - Spring Security集成
  - @Async支持
  - @Retry支持
  - Jackson定制
  - 数据绑定（包含类型转换，格式化，校验）
- 外部框架
  - Redis集成
  - jOOQ集成
  - Druid集成
  - Flyway集成

如果你想进一步了解Spring Boot，可以参考我写的这篇介绍Spring Boot的[Slides](http://slides.com/emacooshen/spring-boot#/)，以及我写的[Spring系列博客](http://emacoo.cn/tags/Spring/)：

- [【Spring】详解Spring MVC中不同格式的POST请求参数的数据类型转换过程](http://emacoo.cn/backend/spring-converter/)
- [【Spring】基于Spring Data JPA的分页组件](http://emacoo.cn/backend/spring-data-jpa-pagination/)
- [【Spring】单应用多数据库的事务管理](http://emacoo.cn/backend/spring-transaction/)
- [【Spring】Redis的两个典型应用场景](http://emacoo.cn/backend/spring-redis/)
- [【Spring】关于Boot应用中集成Spring Security你必须了解的那些事](http://emacoo.cn/backend/spring-boot-security/)
- [【Spring】如何在单个Boot应用中配置多数据库？](http://emacoo.cn/backend/spring-boot-multi-db/)

## 本地运行

1. Clone项目到本地硬盘
1. 安装并启动MySQL实例，创建jpetstore数据库，字符集选择UTF8
1. 下载并安装Redis，使用默认配置启动
1. 导入项目到IDE中，并运行PetstoreApplication

PS：如果以上的内容对你有所帮助，欢迎到我的[留言板](https://github.com/emac/emac.github.io/issues/2)进行交流，或者[赞赏榜](https://github.com/emac/emac.github.io/issues/1)为我点赞。
