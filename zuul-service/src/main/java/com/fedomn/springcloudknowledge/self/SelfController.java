package com.fedomn.springcloudknowledge.self;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SelfController {

  @GetMapping("/self/test")
  public String zuulTest() {
    return "zuul test success";
  }
}
