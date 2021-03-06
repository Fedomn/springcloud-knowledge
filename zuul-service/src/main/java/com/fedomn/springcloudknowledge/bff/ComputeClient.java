package com.fedomn.springcloudknowledge.bff;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient("compute-service")
public interface ComputeClient {
  @GetMapping("add")
  ComputeResponse add(@RequestParam("a") Integer a, @RequestParam("b") Integer b);
}
