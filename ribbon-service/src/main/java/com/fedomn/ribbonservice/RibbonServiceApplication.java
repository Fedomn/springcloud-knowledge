package com.fedomn.ribbonservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class RibbonServiceApplication {

  @Autowired RestTemplate restTemplate;

  public static void main(String[] args) {
    SpringApplication.run(RibbonServiceApplication.class, args);
  }

  @Bean
  @LoadBalanced
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String add() {
    return restTemplate
        .getForEntity("http://COMPUTE-SERVICE/add?a=10&b=20", String.class)
        .getBody();
  }
}
