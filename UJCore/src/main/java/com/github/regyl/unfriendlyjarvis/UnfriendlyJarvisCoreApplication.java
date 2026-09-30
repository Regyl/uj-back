package com.github.regyl.unfriendlyjarvis;

import com.github.regyl.unfriendlyjarvis.grpc.UserSecretDataServiceGrpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.grpc.client.ImportGrpcClients;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "com.github.regyl.unfriendlyjarvis.configuration")
@ImportGrpcClients(target = "auth-service", types = UserSecretDataServiceGrpc.UserSecretDataServiceBlockingStub.class)
public class UnfriendlyJarvisCoreApplication {

    static void main(String[] args) {
        SpringApplication.run(UnfriendlyJarvisCoreApplication.class, args);
    }

}
