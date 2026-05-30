package fello.miage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main du service composite.
 * - @EnableDiscoveryClient : enregistre le service dans Eureka
 * - @EnableFeignClients : active la détection des @FeignClient pour appeler ms-user et ms-cours
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CompositeApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompositeApplication.class, args);
    }
}
