package com.fedomn.computeservice;

import org.springframework.stereotype.Service;

@Service
public class ComputeService {
  public ComputeResponse Add(Integer a, Integer b) {
    Integer addResult = a + b;
    return new ComputeResponse(addResult, true);
  }
}
