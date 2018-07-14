package com.fedomn.computeservice;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class ComputeServiceApplication {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public static void main(String[] args) {
    SpringApplication.run(ComputeServiceApplication.class, args);
  }

  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public Integer add(@RequestParam Integer a, @RequestParam Integer b, HttpServletRequest request) {
    Integer r = a + b;
    logger.info(String.format("/add, host: %s", request.getRemoteHost()));
    return r;
  }
}
