package com.draghicidaniel.login.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		/*http
			.csrf()
			.disable()
			.authorizeHttpRequests()
			.anyRequest()
			.authenticated()
			.and()
			.oauth2Login()
			;
		*/
		/*
		http.authorizeRequests()
        .requestMatchers("/", "/login**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .oauth2Login();
        */
		http.cors()
        .and()
        .authorizeRequests()
        .requestMatchers(HttpMethod.GET, "/user/info", "/api/foos/**")
        .hasAuthority("SCOPE_read")
        .requestMatchers(HttpMethod.POST, "/api/foos")
        .hasAuthority("SCOPE_write")
        .anyRequest()
        .authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt();
		return http.build();
	}
	@Bean
    WebClient webClient(ClientRegistrationRepository clientRegistrationRepository, 
      OAuth2AuthorizedClientRepository authorizedClientRepository) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = 
          new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository, 
          authorizedClientRepository);
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        return WebClient.builder()
            .apply(oauth2.oauth2Configuration())
            .build();
    }
}
