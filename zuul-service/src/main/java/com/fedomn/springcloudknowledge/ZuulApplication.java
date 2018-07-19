package com.fedomn.springcloudknowledge;

import com.fedomn.springcloudknowledge.filters.post.CorsFilter;
import com.fedomn.springcloudknowledge.filters.pre.RequestLogFilter;
import com.fedomn.springcloudknowledge.filters.pre.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class ZuulApplication {

  public static void main(String[] args) {
    SpringApplication.run(ZuulApplication.class, args);
  }

  @Bean
  public TokenFilter tokenFilter() {
    return new TokenFilter();
  }

  @Bean
  public RequestLogFilter requestLogFilter() {
    return new RequestLogFilter();
  }

  @Bean
  public CorsFilter corsFilter() {
    return new CorsFilter();
  }
}
