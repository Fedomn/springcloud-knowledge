package com.fedomn.computeservice;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class ComputeServiceApplication {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ComputeService computeService;

  @Autowired
  public ComputeServiceApplication(ComputeService computeService) {
    this.computeService = computeService;
  }

  public static void main(String[] args) {
    SpringApplication.run(ComputeServiceApplication.class, args);
  }

  @GetMapping("add")
  public ComputeResponse add(
      @RequestParam(value = "a") Integer a,
      @RequestParam(value = "b") Integer b,
      HttpServletRequest request) {
    logger.info(String.format("/add, host: %s", request.getRemoteHost()));
    return computeService.Add(a, b);
  }
}
