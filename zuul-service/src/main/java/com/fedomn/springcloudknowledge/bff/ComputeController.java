package com.fedomn.springcloudknowledge.bff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComputeController {
  private static final Logger logger = LoggerFactory.getLogger(ComputeController.class);

  private final ComputeClient computeClient;

  @Autowired
  public ComputeController(ComputeClient computeClient) {
    this.computeClient = computeClient;
  }

  @GetMapping("/bff/add")
  public ComputeResponse add(@RequestParam("a") Integer a, @RequestParam("b") Integer b) {
    logger.info("GET bff add");
    return computeClient.add(a, b);
  }
}
