package com.fedomn.springcloudknowledge.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtTokenProvider jwtTokenProvider;

  private final CustomerUserDetailsService customerUserDetailsService;

  @Autowired
  public JwtAuthenticationFilter(
      JwtTokenProvider jwtTokenProvider, CustomerUserDetailsService customerUserDetailsService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.customerUserDetailsService = customerUserDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    try {
      String jwtToken = getJwtFromRequest(request);
      if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
        Long userId = jwtTokenProvider.getUserIdFromJWT(jwtToken);
        UserDetails userDetails = customerUserDetailsService.loadUserByUserId(userId);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      try {
        filterChain.doFilter(request, response);
      } catch (IOException | ServletException e) {
        e.printStackTrace();
      }

    } catch (Exception e) {
      logger.error("Could not set user authentication in security context", e);
    }
  }

  public String getJwtFromRequest(HttpServletRequest request) {
    String bearerHeader = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerHeader) && bearerHeader.startsWith("Bearer ")) {
      return bearerHeader.replace("Bearer ", "");
    }
    return null;
  }
}
