package com.fedomn.eurekaserver;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableEurekaServer
@SpringBootApplication
@RestController
public class EurekaServerApplication {
  private final DiscoveryClient discoveryClient;

  public EurekaServerApplication(DiscoveryClient discoveryClient) {
    this.discoveryClient = discoveryClient;
  }

  public static void main(String[] args) {
    SpringApplication.run(EurekaServerApplication.class, args);
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
