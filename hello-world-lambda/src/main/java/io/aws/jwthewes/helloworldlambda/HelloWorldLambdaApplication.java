package io.aws.jwthewes.helloworldlambda;

import io.aws.jwthewes.lambdaextension.ExtensionMain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class HelloWorldLambdaApplication {

    public static void main(String[] args) {
        ExtensionMain extensionMain = new ExtensionMain();
        new Thread(extensionMain).start();
        SpringApplication.run(HelloWorldLambdaApplication.class, args);
    }

    @Bean
    public Function<String, String> reverseString() {
        System.out.println(System.getProperty("java.class.path"));
        return value -> new StringBuilder(value).reverse().toString();
    }

}
