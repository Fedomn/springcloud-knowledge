package com.fedomn.eurekaserver.aws;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ports {
  private String containerPort;
  private String protocol;
  private String hostPort;

  @JsonProperty("ContainerPort")
  public String getContainerPort() {
    return containerPort;
  }

  @JsonProperty("ContainerPort")
  public void setContainerPort(String containerPort) {
    this.containerPort = containerPort;
  }

  @JsonProperty("Protocol")
  public String getProtocol() {
    return protocol;
  }

  @JsonProperty("Protocol")
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  @JsonProperty("HostPort")
  public String getHostPort() {
    return hostPort;
  }

  @JsonProperty("HostPort")
  public void setHostPort(String hostPort) {
    this.hostPort = hostPort;
  }
}
