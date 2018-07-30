package com.fedomn.springcloudknowledge.filters.post;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RequestOutFilter extends ZuulFilter {

  private static final Logger logger = LoggerFactory.getLogger(RequestOutFilter.class);

  @Override
  public String filterType() {
    return POST_TYPE;
  }

  @Override
  public int filterOrder() {
    return 3;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    Instant ReqInTime = (Instant) ctx.get("req-in-time");
    long cost = ChronoUnit.MILLIS.between(ReqInTime, Instant.now());
    logger.info(
        String.format(
            "%s request to %s cost %d ms",
            request.getMethod(), request.getRequestURL().toString(), cost));
    return null;
  }
}
