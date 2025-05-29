package com.mycheque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * The <i>MyCheque REST API</i> application entry point.
 *
 * @author resxnvnce
 */
@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.mycheque.client.configure")
public class MyChequeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyChequeApplication.class, args);
    }
}
