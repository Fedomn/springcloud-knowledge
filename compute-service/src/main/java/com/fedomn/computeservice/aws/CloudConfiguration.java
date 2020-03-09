package com.fedomn.computeservice.aws;

import static org.springframework.cloud.commons.util.IdUtils.getDefaultInstanceId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.metadata.ManagementMetadata;
import org.springframework.cloud.netflix.eureka.metadata.ManagementMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

/**
 * Configuration related to running service on AWS ECS Fargate. We overwrite IP address which is
 * registered to eureka server. We are using endpoint which is defined in aws:
 * https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task-metadata-endpoint.html Note!
 * Ensure that this is used in app profiles which are executed on AWS ECS Fargate.
 */
@Configuration
public class CloudConfiguration {

  private static final String AWS_API_VERSION = "v2";
  private static final String AWS_METADATA_URL =
      "http://169.254.170.2/" + AWS_API_VERSION + "/metadata";
  // Used as string.contains to search correct container
  // Make sure that your Docker container in AWS Task definition has this as part of its name
  private static final String DOCKER_CONTAINER_NAME = "compute-service";
  private final Logger log = LoggerFactory.getLogger(CloudConfiguration.class);
  private final ConfigurableEnvironment env;

  @Autowired
  public CloudConfiguration(ConfigurableEnvironment env) {
    this.env = env;
  }

  /** We run service in fargate so override default IP when in fargate profile */
  @Bean
  public EurekaInstanceConfigBean eurekaInstanceConfig(
      InetUtils inetUtils, ManagementMetadataProvider managementMetadataProvider) {
    log.info("Customize EurekaInstanceConfigBean for AWS");
    log.info("Docker container should have name containing " + DOCKER_CONTAINER_NAME);

    EurekaInstanceConfigBean config = null;
    try {
      String json = readEcsMetadata();
      log.info("Read EcsMetadata: {}", json);
      EcsTaskMetadata metadata = Converter.fromJsonString(json);
      String ipAddress = findContainerPrivateIP(metadata);
      log.info("Override ip address to " + ipAddress);

      config = initEurekaInstanceConfigBean(ipAddress, inetUtils, managementMetadataProvider);
    } catch (Exception ex) {
      log.info("Something went wrong when reading ECS metadata: " + ex.getMessage());
    }
    return config;
  }

  private String findContainerPrivateIP(EcsTaskMetadata metadata) {
    if (null != metadata) {
      for (Container container : metadata.getContainers()) {
        boolean found = container.getName().toLowerCase().contains(DOCKER_CONTAINER_NAME);
        if (found) {
          Network network = container.getNetworks()[0];
          return network.getIPv4Addresses()[0];
        }
      }
    }
    return "";
  }

  private String readEcsMetadata() throws Exception {
    URL obj = new URL(AWS_METADATA_URL);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    StringBuilder response = new StringBuilder();
    try {
      con.setRequestMethod("GET");
      try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
      }
    } finally {
      con.disconnect();
    }
    return response.toString();
  }

  private EurekaInstanceConfigBean initEurekaInstanceConfigBean(
      String eniPrivateIp,
      InetUtils inetUtils,
      ManagementMetadataProvider managementMetadataProvider) {
    String hostname = getProperty("eureka.instance.hostname");
    boolean preferIpAddress =
        Boolean.parseBoolean(getProperty("eureka.instance.prefer-ip-address"));
    String ipAddress = getProperty("eureka.instance.ip-address");
    boolean isSecurePortEnabled =
        Boolean.parseBoolean(getProperty("eureka.instance.secure-port-enabled"));

    String serverContextPath = env.getProperty("server.servlet.context-path", "/");
    int serverPort =
        Integer.parseInt(env.getProperty("server.port", env.getProperty("port", "8080")));

    Integer managementPort = env.getProperty("management.server.port", Integer.class);
    String managementContextPath = env.getProperty("management.server.servlet.context-path");
    EurekaInstanceConfigBean instance = new EurekaInstanceConfigBean(inetUtils);

    instance.setIpAddress(eniPrivateIp);
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    map.put("spring.cloud.client.hostname", eniPrivateIp);
    map.put("spring.cloud.client.ip-address", eniPrivateIp);
    MapPropertySource propertySource = new MapPropertySource("springCloudClientHostInfo", map);
    env.getPropertySources().addLast(propertySource);

    instance.setNonSecurePort(serverPort);
    instance.setInstanceId(getDefaultInstanceId(env));
    instance.setPreferIpAddress(preferIpAddress);
    instance.setSecurePortEnabled(isSecurePortEnabled);
    if (StringUtils.hasText(ipAddress)) {
      instance.setIpAddress(ipAddress);
    }

    if (isSecurePortEnabled) {
      instance.setSecurePort(serverPort);
    }

    if (StringUtils.hasText(hostname)) {
      instance.setHostname(hostname);
    }
    String statusPageUrlPath = getProperty("eureka.instance.status-page-url-path");
    String healthCheckUrlPath = getProperty("eureka.instance.health-check-url-path");

    if (StringUtils.hasText(statusPageUrlPath)) {
      instance.setStatusPageUrlPath(statusPageUrlPath);
    }
    if (StringUtils.hasText(healthCheckUrlPath)) {
      instance.setHealthCheckUrlPath(healthCheckUrlPath);
    }

    ManagementMetadata metadata =
        managementMetadataProvider.get(
            instance, serverPort, serverContextPath, managementContextPath, managementPort);

    if (metadata != null) {
      instance.setStatusPageUrl(metadata.getStatusPageUrl());
      instance.setHealthCheckUrl(metadata.getHealthCheckUrl());
      if (instance.isSecurePortEnabled()) {
        instance.setSecureHealthCheckUrl(metadata.getSecureHealthCheckUrl());
      }
      Map<String, String> metadataMap = instance.getMetadataMap();
      metadataMap.computeIfAbsent(
          "management.port", k -> String.valueOf(metadata.getManagementPort()));
    } else {
      // without the metadata the status and health check URLs will not be set
      // and the status page and health check url paths will not include the
      // context path so set them here
      if (StringUtils.hasText(managementContextPath)) {
        instance.setHealthCheckUrlPath(managementContextPath + instance.getHealthCheckUrlPath());
        instance.setStatusPageUrlPath(managementContextPath + instance.getStatusPageUrlPath());
      }
    }

    return instance;
  }

  private String getProperty(String property) {
    return this.env.containsProperty(property) ? this.env.getProperty(property) : "";
  }
}
