###   关于JSM

### 关于OpenJMS的工程化难度
openjms下载的包中example确实可以通过脚本编译之后，直接通过命令
run Sender "topic1" 10 跑起来。但是我希望能够将这个class文件进行打包，进行工程化的管理尝试。

方案1：maven
由于依赖的lib包已经全部提供。这些包maven仓库中已经没有。或者找不到，或者无法兼容。比如jms-1.1.jar这个包。

并且maven无法直接打包目录依赖的jar包。这条路径尝试了几个小时之后放弃。


方案2：ant
Ant和OpenJMS一样是比较老的技术。Ant相对来说也比较原始，但是好处就在于没有很多的“约定俗成”。几乎可以定制一切东西。
在华为内部很多产品线，依然还在使用ant进行构建。

使用ant之后，大概花了2个小时搞定。

实际上ant也可以进行有层级有组织的模块构建。
比如总的入口:
build.xml 通过external脚本调用起来
  module A : build.xml
  module B : build.xml


### maven-antrun-plugin
这是另外一个思路。
  


