package com.fedomn.springcloudknowledge.security;

import com.fedomn.springcloudknowledge.domain.User;
import com.fedomn.springcloudknowledge.domain.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return UserPrincipal.create(new User(1L, "test", "test"));
  }

  public UserDetails loadUserByUserId(Long id) throws UsernameNotFoundException {
    return UserPrincipal.create(new User(1L, "test", "test"));
  }
}
