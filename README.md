# CyberChair 2077

![](https://img.shields.io/badge/open%20jdk-1.8-blue)
![](https://img.shields.io/badge/springboot-2.2.5-blue)
![](https://img.shields.io/badge/vue-2.9.6-yellow)
![](https://img.shields.io/github/repo-size/httdty/cyber-chair)
![](https://img.shields.io/github/languages/top/httdty/cyber-chair)


本项目是高级软件工程的作业，主要实现了一个论文投递的website，主要技术栈使用Spring Boot和Vue进行项目开发，项目架构采用微服务架构，目前已经部署到K8S集群上

## PreRequisite

- docker环境
    - 安装参考[docker install](https://www.docker.com/get-started)
- kubectl
    - 安装参考[install-kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
- 配置集群凭据
    - 按照助教所给的pdf文件进行kube config配置

## File Structure

```text
cyber-chair/
    cc-admin-meeting-service/  #admin-meeting服务
    cc-author-article-service/ #author-article服务
    cc-chair-service/          #chair服务
    cc-eureka-service/         #eureka server
    cc-gateway-service/        #gateway网关servier
    cc-pcmember-service/       #pcmember服务
    cc-ui-service/             #前端源代码
    cc-user-auth-service/      #user-auth服务
    data/                      #h2数据库文件
    k8s/                       #k8s的部署配置文件
```

## Get Started

我们提供了三种方式用于运行我们的项目

### 三种方式

#### 1. native run

为每个java项目打包
```bash
mvn clean package
```

运行每个service下的target中jar包和启动前端项目，推荐按照如下顺序运行
```shell script
java -jar cc-eureka-service-1.0.jar
java -jar cc-gateway-service-1.0.jar
cd cc-ui-service/vue-src && npm install && vue-cli-service serve --open
java -jar cc-admin-meeting-service-1.0.jar
java -jar cc-author-article-service-1.0.jar
java -jar cc-chair-service-1.0.jar
java -jar cc-pcmember-service-1.0.jar
java -jar cc-user-auth-service-1.0.jar
```

#### 2. docker-compose

使用docker-compose来一键启动所有项目
0. 打包前端
    ```shell script
    cd cc-ui-service/vue-src && vue-cli-service build --dest ../static
    ```
1. 确保处于cyber-chair根目录下，即该目录存在docker-compose.yml文件
2. 运行命令
    ```shell script
    # start
    docker-compose up
    # shutdown
    docker-compose down
    ```
3. 启动后可以看到docker images有对应的镜像
    ![image.png](https://i.loli.net/2020/12/13/FLC14IEJGSlhQzD.png)

#### 3. k8s deploy

使用k8s部署配置文件来一键部署到阿里云ASK集群上
0. 打包前端
    ```shell script
    cd cc-ui-service/vue-src && vue-cli-service build --dest ../static
    ```
1. 确保处于k8s目录下，该目录存在`cc-service.yaml`文件
2. 运行命令
    ```shell script
    # start
    kubectl apply -f cc-service.yaml
    # delete
    kubectl delete -f cc-service.yaml
    ```
3. 启动成功
    - 调用`kubectl get svc -n ase-ns-03`可以看到如下结果
        ![image.png](https://i.loli.net/2020/12/13/tbDkVFx2TWoGyU5.png)
    - 调用`kubectl get pod -n ase-ns-03`看到如下结果
        ![image.png](https://i.loli.net/2020/12/13/AUixaLbhdNRnq8z.png)
      