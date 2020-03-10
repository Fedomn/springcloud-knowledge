package com.fedomn.eurekaserver.configs;

import static org.springframework.cloud.netflix.eureka.EurekaClientConfigBean.DEFAULT_ZONE;
import static org.springframework.cloud.netflix.eureka.EurekaConstants.DEFAULT_PREFIX;

import com.fedomn.eurekaserver.aws.EcsTaskMetadata;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration related to running service on AWS ECS Fargate. We overwrite IP address which is registered to eureka server. We are using endpoint
 * which is defined in aws: https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task-metadata-endpoint.html Note! Ensure that this is used in
 * app profiles which are executed on AWS ECS Fargate.
 */
@Configuration
public class AwsEurekaClientConfig {

  private static final String DOCKER_CONTAINER_NAME = "eureka";
  private static final String SERVER_PORT = "server.port";
  private final Logger log = LoggerFactory.getLogger(AwsEurekaClientConfig.class);
  private final ConfigurableEnvironment environment;
  private final RestTemplate restTemplate;

  @Autowired
  public AwsEurekaClientConfig(ConfigurableEnvironment environment, RestTemplate restTemplate) {
    this.environment = environment;
    this.restTemplate = restTemplate;
  }

  /**
   * We run service in fargate so custom eureka registry urls when in fargate profile
   */
  @Bean
  public EurekaClientConfigBean eurekaClientConfigBean() {
    log.info("Customize EurekaClientConfigBean for AWS");
    log.info("Docker container should have name containing " + DOCKER_CONTAINER_NAME);

    EurekaClientConfigBean config = new EurekaClientConfigBean();
    String awsMetadataUrl = System.getenv("ECS_CONTAINER_METADATA_URI") + "/task";
    String serverUrls = buildServerUrls(awsMetadataUrl);
    log.info("Default registry urls: " + serverUrls);

    Map<String, String> serverUrl = new HashMap<>();
    serverUrl.put(DEFAULT_ZONE, serverUrls);
    config.setServiceUrl(serverUrl);
    return config;
  }

  private String buildServerUrls(String url) {
    EcsTaskMetadata tasks = this.restTemplate.getForObject(url, EcsTaskMetadata.class);
    if (tasks == null) {
      return "";
    }
    StringBuilder urlBuilder = new StringBuilder();
    int serverPort = environment.getProperty(SERVER_PORT, Integer.class);
    Arrays.stream(tasks.getContainers())
        .filter(Objects::nonNull)
        .filter(container -> container.getName().toLowerCase().contains(DOCKER_CONTAINER_NAME))
        .forEach(container -> {
          urlBuilder.append("http://");
          urlBuilder.append(container.getNetworks()[0].getIPv4Addresses()[0]);
          urlBuilder.append(":");
          urlBuilder.append(serverPort);
          urlBuilder.append(DEFAULT_PREFIX);
          urlBuilder.append("/,");
        });

    return urlBuilder.substring(0, urlBuilder.length()-1);
  }

}
