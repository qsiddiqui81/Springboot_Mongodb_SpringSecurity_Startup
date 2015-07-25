package com.newDemo.modules.user.springsecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.newDemo.modules.user.service.AuthenticationTokenService;
import com.newDemo.modules.user.springsecurity.filters.AuthenticationFilter;
import com.newDemo.modules.user.springsecurity.providers.AuthProvider;
import com.newDemo.modules.user.springsecurity.providers.TokenAuthenticationProvider;


@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	public static Logger log = LoggerFactory.getLogger(SecurityConfig.class);
	
    @Override
    public void configure(WebSecurity web) throws Exception {
    	//TODO ignore unsecure urls
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	 http
    	 .csrf().disable().
         sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
         .and()
         .authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/user").permitAll()
         .anyRequest().authenticated();
         http.addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }
    
    @Bean
    public AuthenticationTokenService tokenService() {
        return new AuthenticationTokenService();
    }

    @Bean
    public AuthenticationProvider domainUsernamePasswordAuthenticationProvider() {
        return new AuthProvider(tokenService());
    }
    
    @Bean
    public AuthenticationProvider tokenAuthenticationProvider() {
        return new TokenAuthenticationProvider(tokenService());
    }
   
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(domainUsernamePasswordAuthenticationProvider()).
        authenticationProvider(tokenAuthenticationProvider());
    }
}