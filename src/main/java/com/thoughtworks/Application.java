package com.thoughtworks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableAutoConfiguration
@EntityScan
@EnableJms
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
