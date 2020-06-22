# jenkins简单配置
## 插件
安装以下插件
git，maven

jenkins主机需要与远程主机配置主机信任，以便scp传包
## 新建view
选择freeStyle就可以
填入代码的git仓库地址，账号密码，分支（例如： */develop）
## 脚本
```shell
#设置微服务名称。后续脚本中的发布路径以及jar包均与此名称相关
APP_NAME="you_app_name"
SP_PROFILE="-Dspring.profiles.active=sit"
#堆内存大小
HEAP_SETTING="-Xms512m -Xmx512m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=512m -XX:CompressedClassSpaceSize=128m"

#目标服务器名称
TARGET_SERVER="your_target_server"
TARGET_PATH=${APP_BASE_DIR}/${APP_NAME}

#GC日志与dump file 设置
GC_LOG_BASE_PATH=${TARGET_PATH}/gclogs
GC_LOG=${GC_LOG_BASE_PATH}/gc.log
DUMP_FILE=${GC_LOG_BASE_PATH}/${APP_NAME}.dump


#打包，打完包，应该产出${APP_NAME}-run.jar, 否则请修改pom.xml文件
mvn clean package -DskipTests

JAR_FILE="${APP_NAME}-run.jar"

#验证${APP_NAME}-run.jar包是否存在
if [ ! -f target/$JAR_FILE ]; then 
 echo "打包完成后，必须生成【${JAR_FILE}】文件，请修改pom.xml文件后再执行此操作。"
 exit -1;
fi

#保证目标服务器中存在发布路径
ssh -tt root@$TARGET_SERVER << EOF
  mkdir -p $TARGET_PATH
  mkdir -p $GC_LOG_BASE_PATH
  exit 0
EOF

#复制文件到远程目标文件夹
scp -r target/$JAR_FILE root@${TARGET_SERVER}:${TARGET_PATH}



# 把原进程结束掉，如果有的话

PID=$(ssh root@$TARGET_SERVER "ps -ef | grep $JAR_FILE | grep -v grep" | awk '{print $2}')
echo "existed PID: ${PID}"
if [ ${PID}x != x ]; then
	echo "Shutting down $APP_NAME service PID[$PID]..."
	ssh root@$TARGET_SERVER "kill $PID"
	echo "Waiting 10 seconds..."
	sleep 10
fi

ssh -tt root@$TARGET_SERVER << EOF
  
  if [ -f ${GC_LOG} ]; then
    mv $GC_LOG ${GC_LOG}.`date +'%Y%m%d_%H%M%S'`
  fi
  
  if [ -f ${DUMP_FILE} ]; then
      mv $DUMP_FILE ${DUMP_FILE}.`date +'%Y%m%d_%H%M%S'`
  fi
  
  #启动微服务
  cd ${TARGET_PATH}
  echo "Starting $APP_NAME service ..."
  nohup java -server ${HEAP_SETTING} -XX:NewRatio=1 -XX:+UseConcMarkSweepGC \
 -XX:+CMSClassUnloadingEnabled -XX:CMSMaxAbortablePrecleanTime=5000 -XX:CMSInitiatingOccupancyFraction=80 -XX:+UseCompressedOops \
 -XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${DUMP_FILE} -verbose:gc \
 -Xloggc:${GC_LOG} -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Djava.awt.headless=true \
 -Dsun.net.client.defaultConnectTimeout=60000 \
 -Djava.awt.headless=true -jar $SP_PROFILE $JAR_FILE > /dev/null &
 
 exit 0
EOF
```
