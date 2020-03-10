package com.fedomn.eurekaserver.aws;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LabelsLocal {
  private String comAmazonawsEcsCluster;
  private String comAmazonawsEcsContainerName;
  private String comAmazonawsEcsTaskArn;
  private String comAmazonawsEcsTaskDefinitionFamily;
  private String comAmazonawsEcsTaskDefinitionVersion;
  private String comAmazonawsEcsTaskDefinitionVersion1;
  private String comAmazonawsEcsTaskDefinitionVersion2;
  private String comAmazonawsEcsTaskDefinitionVersion3;

  @JsonProperty("com.docker.compose.config-hash")
  public String getCOMAmazonawsEcsCluster() {
    return comAmazonawsEcsCluster;
  }

  @JsonProperty("com.docker.compose.config-hash")
  public void setCOMAmazonawsEcsCluster(String value) {
    this.comAmazonawsEcsCluster = value;
  }

  @JsonProperty("com.docker.compose.container-number")
  public String getCOMAmazonawsEcsContainerName() {
    return comAmazonawsEcsContainerName;
  }

  @JsonProperty("com.docker.compose.container-number")
  public void setCOMAmazonawsEcsContainerName(String value) {
    this.comAmazonawsEcsContainerName = value;
  }

  @JsonProperty("com.docker.compose.oneoff")
  public String getCOMAmazonawsEcsTaskArn() {
    return comAmazonawsEcsTaskArn;
  }

  @JsonProperty("com.docker.compose.oneoff")
  public void setCOMAmazonawsEcsTaskArn(String value) {
    this.comAmazonawsEcsTaskArn = value;
  }

  @JsonProperty("com.docker.compose.project")
  public String getCOMAmazonawsEcsTaskDefinitionFamily() {
    return comAmazonawsEcsTaskDefinitionFamily;
  }

  @JsonProperty("com.docker.compose.project")
  public void setCOMAmazonawsEcsTaskDefinitionFamily(String value) {
    this.comAmazonawsEcsTaskDefinitionFamily = value;
  }

  @JsonProperty("com.docker.compose.project.config_files")
  public String getCOMAmazonawsEcsTaskDefinitionVersion() {
    return comAmazonawsEcsTaskDefinitionVersion;
  }

  @JsonProperty("com.docker.compose.project.config_files")
  public void setCOMAmazonawsEcsTaskDefinitionVersion(String value) {
    this.comAmazonawsEcsTaskDefinitionVersion = value;
  }

  @JsonProperty("com.docker.compose.project.working_dir")
  public String getComAmazonawsEcsTaskDefinitionVersion1() {
    return comAmazonawsEcsTaskDefinitionVersion1;
  }

  @JsonProperty("com.docker.compose.project.working_dir")
  public void setComAmazonawsEcsTaskDefinitionVersion1(String comAmazonawsEcsTaskDefinitionVersion1) {
    this.comAmazonawsEcsTaskDefinitionVersion1 = comAmazonawsEcsTaskDefinitionVersion1;
  }

  @JsonProperty("com.docker.compose.service")
  public String getComAmazonawsEcsTaskDefinitionVersion2() {
    return comAmazonawsEcsTaskDefinitionVersion2;
  }

  @JsonProperty("com.docker.compose.service")
  public void setComAmazonawsEcsTaskDefinitionVersion2(String comAmazonawsEcsTaskDefinitionVersion2) {
    this.comAmazonawsEcsTaskDefinitionVersion2 = comAmazonawsEcsTaskDefinitionVersion2;
  }

  @JsonProperty("com.docker.compose.version")
  public String getComAmazonawsEcsTaskDefinitionVersion3() {
    return comAmazonawsEcsTaskDefinitionVersion3;
  }

  @JsonProperty("com.docker.compose.version")
  public void setComAmazonawsEcsTaskDefinitionVersion3(String comAmazonawsEcsTaskDefinitionVersion3) {
    this.comAmazonawsEcsTaskDefinitionVersion3 = comAmazonawsEcsTaskDefinitionVersion3;
  }
}
