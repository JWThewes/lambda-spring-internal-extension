package io.aws.jwthewes.helloworldlambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class HelloWorldLambdaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldLambdaApplication.class);

    public static void main(String[] args) {
//        LOGGER.info("starting application");
//        new Thread(new ExtensionMain()).start();
//        SpringApplication.run(HelloWorldLambdaApplication.class, args);
    }

    @Bean
    public ThreadLauncherBean threadLauncherBean() throws InterruptedException {
        LOGGER.info("starting thread launcher bean");
        return new ThreadLauncherBean();
    }

    @Bean
    public Function<String, String> reverseString(ThreadLauncherBean threadLauncherBean) {
        return value -> new StringBuilder(value).reverse().toString();
    }

}
