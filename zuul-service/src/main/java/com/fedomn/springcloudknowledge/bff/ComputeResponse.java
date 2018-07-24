package com.fedomn.springcloudknowledge.bff;

public class ComputeResponse {
  private Integer ComputeResult;
  private Boolean Status;

  public ComputeResponse() {}

  public ComputeResponse(Integer computeResult, Boolean status) {
    ComputeResult = computeResult;
    Status = status;
  }

  public Integer getComputeResult() {
    return ComputeResult;
  }

  public void setComputeResult(Integer computeResult) {
    ComputeResult = computeResult;
  }

  public Boolean getStatus() {
    return Status;
  }

  public void setStatus(Boolean status) {
    Status = status;
  }
}
