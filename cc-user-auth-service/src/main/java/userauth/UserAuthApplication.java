package userauth;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import userauth.domain.User;
import userauth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.client.RestTemplate;

@EnableEurekaClient
@SpringBootApplication
public class UserAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserAuthApplication.class, args);
    }

    /**
     * This is a function to create some basic entities when the application starts.
     * Now we are using a In-Memory database, so you need it.
     * You can change it as you like.
     */
    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                // Create authorities if not exist.

                // Create an admin if not exists.
                if (userRepository.findByUsername("admin") == null) {
                    User admin = new User(
                            "admin",
                            "ErangelManager",
                            BCrypt.hashpw("Erangel", BCrypt.gensalt()),
                            "libowen@fudan.edu.cn",
                            "Fudan University",
                            "ShangHai China"
                    );
                    userRepository.save(admin);
                }
                if (userRepository.findByUsername("test123") == null) {
                    User testUser = new User(
                            "test123",
                            "test",
                            BCrypt.hashpw("12345qwert", BCrypt.gensalt()),
                            "test@123.com",
                            "fudan",
                            "shanghai"
                    );
                    userRepository.save(testUser);
                }
            }


        };
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
