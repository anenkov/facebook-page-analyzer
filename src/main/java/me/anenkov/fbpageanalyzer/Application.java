package me.anenkov.fbpageanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main app
 *
 * @author anenkov
 */
@ComponentScan
@EnableAutoConfiguration
@SuppressWarnings("squid:S1118")
public class Application {
    /**
     * Main entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); //NOSONAR
    }
}
