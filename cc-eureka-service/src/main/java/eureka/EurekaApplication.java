package eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * TODO write your description here
 *
 * @author Ray
 * @version 1.0
 * @projectName lab2
 * @date 2020/11/06 20:56
 * @email httdty2@163.com
 * @software IntelliJ IDEA
 */

@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(eureka.EurekaApplication.class, args);
    }

}