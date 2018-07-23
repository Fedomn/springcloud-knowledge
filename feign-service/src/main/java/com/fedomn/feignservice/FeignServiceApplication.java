package com.fedomn.feignservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RestController
public class FeignServiceApplication {

  private final ComputeClient computeClient;

  @Autowired
  public FeignServiceApplication(ComputeClient computeClient) {
    this.computeClient = computeClient;
  }

  public static void main(String[] args) {
    SpringApplication.run(FeignServiceApplication.class, args);
  }

  @GetMapping("/add")
  public ComputeResponse add(@RequestParam("a") Integer a, @RequestParam("b") Integer b) {
    return computeClient.add(a, b);
  }
}
