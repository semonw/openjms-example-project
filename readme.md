###   启动服务
解压openjms-0.7.7-beta-1.zip
然后通过cmd直接start.sh即可，可以看到已经在监听端口。
也可以打开admin.bat 然后通过菜单Actions启动

### 编译
直接在根目录下进行编译，输入
<code>
ant
</code>

或者ant -f build.xml

###启动listener.jar
默认openJMS会开一个topic1的destination。所有直接连接就行。

java -jar listener.jar "topic1"



### 启动sender.jar

java -jar listener.jar "topic1"

### 参考
openjms文档[http://openjms.sourceforge.net/usersguide/using.html]
