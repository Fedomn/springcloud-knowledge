package com.fedomn.feignservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RestController
public class FeignServiceApplication {

  @Autowired private ComputeClient computeClient;

  public static void main(String[] args) {
    SpringApplication.run(FeignServiceApplication.class, args);
  }

  @GetMapping("/add")
  public Integer add() {
    return computeClient.add(10, 20);
  }

  @Component
  @FeignClient("compute-service")
  interface ComputeClient {
    @GetMapping("add")
    Integer add(@RequestParam Integer a, @RequestParam Integer b);
  }
}
