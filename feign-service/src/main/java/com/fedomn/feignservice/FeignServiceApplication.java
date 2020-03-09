package com.fedomn.feignservice;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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
  private final DiscoveryClient discoveryClient;

  @Autowired
  public FeignServiceApplication(ComputeClient computeClient, DiscoveryClient discoveryClient) {
    this.computeClient = computeClient;
    this.discoveryClient = discoveryClient;
  }

  public static void main(String[] args) {
    SpringApplication.run(FeignServiceApplication.class, args);
  }

  @GetMapping("/add")
  public ComputeResponse add(@RequestParam("a") Integer a, @RequestParam("b") Integer b) {
    return computeClient.add(a, b);
  }

  @GetMapping("/instances")
  public List<List<ServiceInstance>> serviceInstancesByApplicationName() {
    List<List<ServiceInstance>> res = new ArrayList<>();
    List<String> services = discoveryClient.getServices();
    for (String service : services) {
      res.add(discoveryClient.getInstances(service));
    }
    return res;
  }
}
