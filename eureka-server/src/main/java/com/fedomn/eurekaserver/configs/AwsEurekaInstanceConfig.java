package com.fedomn.eurekaserver.configs;

import com.fedomn.eurekaserver.aws.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
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
public class AwsEurekaInstanceConfig {

  private static final String DOCKER_CONTAINER_NAME = "eureka";
  private static final String SERVER_PORT = "server.port";
  private final Logger log = LoggerFactory.getLogger(AwsEurekaInstanceConfig.class);
  private final ConfigurableEnvironment environment;
  private final RestTemplate restTemplate;

  @Autowired
  public AwsEurekaInstanceConfig(ConfigurableEnvironment environment, RestTemplate restTemplate) {
    this.environment = environment;
    this.restTemplate = restTemplate;
  }

  /**
   * We run service in fargate so override default IP when in fargate profile
   */
  @Bean
  public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
    log.info("Customize EurekaInstanceConfigBean for AWS");
    log.info("Docker container should have name containing " + DOCKER_CONTAINER_NAME);

    EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
    String awsMetadataUrl = System.getenv("ECS_CONTAINER_METADATA_URI");
    String ipAddress = getContainerPrivateIp(awsMetadataUrl);
    log.info("Override ip address to " + ipAddress);
    config.setIpAddress(ipAddress);
    config.setNonSecurePort(environment.getProperty(SERVER_PORT, Integer.class));
    config.setPreferIpAddress(true);
    return config;
  }

  private String getContainerPrivateIp(String url) {
    Container container = this.restTemplate.getForObject(url, Container.class);
    if (container == null || !container.getName().toLowerCase().contains(DOCKER_CONTAINER_NAME)) {
      return "";
    }
    return container.getNetworks()[0].getIPv4Addresses()[0];
  }
}
