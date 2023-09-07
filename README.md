# logback-kafka-appender

基于[danielwegener/logback-kafka-appender](https://github.com/danielwegener/logback-kafka-appender)进行的扩展开发.
* 支持spring boot属性配置，集成配置中心则配置热修改
* 支持关闭或开启某些appender
* 支持降级appender
因此这个项目的使用场景相对原版会窄一点，更适用于spring boot项目，非spring boot项目，优势不多。如果是spring boot项目并且集成了配置中心，则配置热修改会带来更多便利
e.g. logback配置日志输入console、file、kafka，则可以通过配置属性在运行中指定某些appender不生效，比如关闭file输出，只输出日志到kafka等

## 集成spring boot
````java
@EnableLogbackConfigDiscover
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
````

## 依赖
```xml
[maven pom.xml]
<dependency>
    <groupId>com.github.danielwegener</groupId>
    <artifactId>logback-kafka-appender</artifactId>
    <version>EH1.0-SNAPSHOT</version>
    <scope>runtime</scope>
</dependency>
<!-- 如果是spring boot默认是依赖logback的  -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
    <scope>runtime</scope>
</dependency>
```

logback配置
```xml
[src/main/resources/logback.xml]
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <!-- 日志目录   -->
    <property name="LOG_HOME" value="${user.dir}/logs"/>
    <!--    日志文件名-->
    <property name="APP_NAME" value="demo"/>

    <!--    使用默认的输出格式-->
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    <include resource="org/springframework/boot/logging/logback/console-appender.xml"/>

    <appender name="DefaultAppender"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_HOME}/${APP_NAME}.log</file>
        <append>true</append>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_HOME}/${APP_NAME}.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>3</maxHistory>
            <totalSizeCap>2GB</totalSizeCap>
            <cleanHistoryOnStart>true</cleanHistoryOnStart>
        </rollingPolicy>
        <encoder>
            <pattern>${FILE_LOG_PATTERN}</pattern>
            <charset class="java.nio.charset.Charset">UTF-8</charset>
        </encoder>
    </appender>
    <!--    异步输出-->
    <appender name="AsyncFileAppender" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="DefaultAppender"/>
        <discardingThreshold>0</discardingThreshold>
    </appender>

    <!-- 日志信息发送kafka -->
    <appender name="KafkaAppender" class="com.github.danielwegener.logback.kafka.KafkaAppender">
        <encoder>
            <pattern>${FILE_LOG_PATTERN}</pattern>
            <charset class="java.nio.charset.Charset">UTF-8</charset>
        </encoder>
        <fallbackAppender>AsyncFileAppender</fallbackAppender>
    </appender>

    <appender name="AsyncKafkaAppender" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="KafkaAppender"/>
    </appender>

    <root>
        <level value="INFO"/>
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="AsyncFileAppender"/>
        <appender-ref ref="AsyncKafkaAppender"/>
    </root>
</configuration>
```

## application.yml配置
```yaml
[src/main/resources/application.yml]
logback:
    kafka:
        # 是否允许输出日志到kafka，false是不允许
        hide-self: false
        # 隐藏appender，不要隐藏自己，通过上面的hide-self来控制自身打印行为
        hide-appender:
            - AsyncFileAppender
        #      - CONSOLE
        producer:
            # 生产者的属性配置，key就是kafka本身的属性
            bootstrap.servers: localhost:9092
            linger.ms: 3000
            acks: 0
            batch.size: 524288
```

## FAQ
Q:  ?  
A: 本身的实现和介绍查看：https://github.com/danielwegener/logback-kafka-appender

## License

This project is licensed under the [Apache License Version 2.0](LICENSE).

