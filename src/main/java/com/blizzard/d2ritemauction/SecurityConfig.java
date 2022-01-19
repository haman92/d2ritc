package com.blizzard.d2ritemauction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

/**
 * Customization of Spring Security, enabling OAuth2 login for all requests and providing a logout handler to ensure
 * proper session destruction.
 *
 * @author Tyler Gregory
 * @since 6/16/2021
 */
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;

	/**
	 * Creates a logout handler to invoke the Battle.net logout endpoint. This will ensure the user is actually logged
	 * out of their Battle.net account before returning to the website. If this is not done, Battle.net login will still
	 * have an active session for the user and they will be auto logged into that account.
	 */
	private LogoutSuccessHandler logoutSuccessHandler() {
		return (request, response, authentication) -> {
			final UriComponents logoutRedirect = UriComponentsBuilder.fromHttpUrl("https://battle.net")
					.pathSegment("login", "logout")
					.queryParam("ref", "http://114.207.147.202:8080/search") // replace this with the actual endpoint of your service
					.build();
			response.sendRedirect(logoutRedirect.toString());
			//System.out.println(logoutRedirect.toString());
		};
	}

	/**
	 * Ensures all requests are authorized. You may want to have a home page where the user isn't asked to login.
	 * Configures OAuth 2.0 Client support. Leverages OAuth 2.0 Authorization Code Grant Flow for login. And uses the
	 * logout handler created above for redirecting users after a successful logout.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//.hasRole("USER")
		/*
		 * 

		 */
		http
					.csrf() // 추가
					.ignoringAntMatchers("/h2/**").disable() // 추가
					.httpBasic()
				.and()
				.headers().frameOptions().disable()
				
				.and()
					.oauth2Login()
					 	
						.userInfoEndpoint()
							.oidcUserService(customOAuth2UserService)
							.and()
						//.redirectionEndpoint()
                     		//.baseUri("/")
                     	
        		
				.and()
					.oauth2Client()
				.and()
					.logout()
						.logoutSuccessUrl("/")
						
						//.logoutSuccessHandler(logoutSuccessHandler())
				.and()
					.authorizeRequests()
							.antMatchers("/h2/**").hasRole("ADMIN")
							.antMatchers("/","/css/**","/js/**","/search","/inputitem","/getitemfilter","/getddbitem","/error/**").permitAll()
							.antMatchers("/mypage","/**").hasRole("USER")
											
				;
				
	}


}
