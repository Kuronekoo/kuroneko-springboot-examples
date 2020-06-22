# vwop发布jar 至mvn 私服仓库说明

## 前提环境

1. 安装jdk 1.8,  maven 3.3.0 以上版本

2. 准备setting.xml文件， 内容如 settings-nexus.xml 所示, 配置中包含 `your-dev`，`your-release` 两个profile



## 发布流程说明

### 发布前的准备

1. 以 common 项目为例， 更新pom.xml中的版本号（project节点下的version节点）。
2. 用命令行方式进入 common工程根目录
3. 运行 mvn clean test 命令， 保证所有测试用例均执行通过。 



### 发布snapshot版本

1. 将common项目的pom.xml 版本号改为 xxx.xx-snapshot
2. 运行命令 `mvn clean deploy -DskipTests -Pyour-dev`
3. 若执行上述命令成功， 即为发布 snapshot 版本成功。 
4. 注意： 相同snapshot 版本 可以 重复发布，且版本号需要加 `-snapshot` 或` -SNAPSHOT`后缀。 



### 发布release版本

1. 将common项目的pom.xml 版本号改为 xxx.xx-release
2. 运行命令 ` mvn clean deploy -DskipTests -Pyour-release`
3. 若执行上述命令成功， 即为发布 release 版本成功。 
4. 注意： 相同release 版本 <span style='color:red'>不可以</span> 重复发布。 版本号不带 `-snapshot` 或` -SNAPSHOT`后缀的， 均视为release 版本。 

