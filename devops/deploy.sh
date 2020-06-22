cd /home/xxx/deploy
scp xxx@xxx-node1:/home/xxx/updates/xxxx-prod*.jar .
ps -ef | grep 'server.port=8080' | grep -v grep| awk '{print $2}' | xargs kill -9
sleep 2
echo "mv"
mv xxxx*.jar xxxx.jar
cd /home/xxxx/service1
echo "delete"
rm -rf  xxxx.jar
mv deploy/xxxx.jar .
echo "start"
nohup java -server -Xms4096m -Xmx4096m -Xmn1536m -Xss512k -XX:-UseParallelGC -XX:NewRatio=1 -XX:SurvivorRatio=8 -XX:MetaspaceSize=1024m -XX:MaxMetaspaceSize=1024m  -jar xxxx.jar --server.port=8080 --spring.profiles.active=prod > /dev/null 2>&1 &
echo Start Success!
tail -f /xxxx/logs/xxxx.8080.log