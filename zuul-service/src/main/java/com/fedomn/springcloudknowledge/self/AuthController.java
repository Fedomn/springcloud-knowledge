package com.fedomn.springcloudknowledge.self;

import com.fedomn.springcloudknowledge.domain.LoginRequest;
import com.fedomn.springcloudknowledge.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  private final AuthenticationManager authenticationManager;

  private final JwtTokenProvider jwtTokenProvider;

  @Autowired
  public AuthController(
      AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @PostMapping("/self/sign-in")
  public ResponseEntity signIn(@RequestBody LoginRequest loginRequest) {
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), loginRequest.getPassword());
    String token;
    try {
      Authentication authenticate = authenticationManager.authenticate(authentication);
      SecurityContextHolder.getContext().setAuthentication(authenticate);
      token = jwtTokenProvider.generateToken(authenticate);
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }
    return ResponseEntity.ok(token);
  }
}
