# 一些说明
# 数据库
1. mysql5及之前的版本使用的是旧版驱动"com.mysql.jdbc.Driver"，mysql6以及之后的版本需要更新到新版驱动，对应的Driver是"com.mysql.cj.jdbc.Driver"，但是这个驱动错误的信息是"Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary."，排除这个原因。
2. 连接数据库的url中，加上allowPublicKeyRetrieval=true参数。
## springcloud-template
### 基础环境及规范约定
1. `jdk` 版本1.8
2. `maven` 版本3.0+，且需在本地setting.xml中配置好maven私服地址
3. IDEA需安装阿里巴巴开发规范插件，lombok插件 

### 层级划分
##### web层
展示层，分为rest, feign, callback三个子文件夹;内部实现必须要调用biz层暴漏的spring bean
其中：
>> 1. rest文件夹代表给H5前端，或者APP客户端提供的接口，其中类名以Controller结尾; 
>> 2. feign文件夹内代表提供给其它子系统的feign接口，类名以FeignControler结尾; 
>> 3. callback文件夹内提供给三方系统的回调接口，类名以CallbackController结尾

##### service层
业务逻辑抽象层，接口名以Service结尾，类名以ServiceImpl结尾；内部实现是调用manager层暴露的spring bean

##### manager层
业务逻辑层，分为impl, mq, schedule三个子文件夹;
其中：
>> 1. impl是代表业务逻辑的实现类，类名以ManagerImpl结尾，内部实现必须要调用dal层暴漏的spring bean；
>> 2. mq文件夹包含了发送MQ消息的bean，接收MQ消息的bean；
>> 3. schedule文件夹代表定时任务，里面的类名以Job结尾

##### dal层
数据库层，分为entity, mapper2个文件夹，分表包含了数据库实体和mybatis的mapper接口
**注意：**
>>1. `mybatis-generator`插件使用需在根目录下，执行`mvn mybatis-generator:generate`
>>2. service层调用dal层时，对生成类Example的使用可参考[Mybatis-Generator生成类Exmaple使用文档]
>>3. 分页插件用的是Mybatis的PageHelper插件，使用的是springboot集成版的

##### client层
调用集成层，分为facade，rest2个文件夹，其中：
>> 1. facade文件夹包含了调用内部系统的spring bean, 实现类以FacadeClientImp结尾，接口以FacadeClient结尾（调用feign接口）
>> 2. rest文件夹包含了调用三方系统的spring bean，实现类以RestClientImpl结尾，接口以RestClient结尾

##### config层
配置层，包含了各个依赖组件自动创建bean的配置类，初步包含了db, ons, property文件夹
>>1. db文件夹中包含了数据库层bean的初始化
>>2. redis文件夹中包含了Redis相关的bean初始化
>>3. property文件夹中包含了接收系统配置的bean，类名以ConfigProperty结尾

##### resource层
资源文件层，包含了bootstrap-${profile}.yml，日志配置文件logback-spring.xml以及mybatis的sql的xml文件

##### 部署
**注意**：线上环境和测试环境启动时都需指定profile，分别会使用`bootstrap-prod.yml`，`bootstrap-test.yml`，本地开发无需配置，默认会使用`bootstrap-default.yml`或`bootstrap.yml`
指定profile:
``` 
java -jar springcloud.jar -Dspring.profiles.active=prod
```




