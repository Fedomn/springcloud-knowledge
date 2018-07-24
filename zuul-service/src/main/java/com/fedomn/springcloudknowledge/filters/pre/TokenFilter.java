package com.fedomn.springcloudknowledge.filters.pre;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenFilter extends ZuulFilter {

  private static final Logger logger = LoggerFactory.getLogger(TokenFilter.class);

  @Override
  public String filterType() {
    return PRE_TYPE;
  }

  @Override
  public int filterOrder() {
    return 2;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    String token = request.getParameter("token");
    if (StringUtils.isBlank(token)) {
      logger.warn("access token is empty");
      ctx.setResponseStatusCode(401);
      throw new RuntimeException("invalid token");
    } else {
      logger.info("access token success");
    }
    return null;
  }
}
