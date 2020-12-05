package adminmeeting.adminmeeting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Config for Swagger
 *
 * @author Ray
 * @version 1.0
 * @projectName lab2
 * @date 2020/11/06 20:56
 * @email httdty2@163.com
 * @software IntelliJ IDEA
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("adminmeeting")) // Application的目录
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Meeting 和 Admin 服务的Rest API文档")
                .description("提供 Meeting 以及 Admin 的相关操作")
                .version("1.0")
                .build();
    }
}