package com.blizzard.d2ritemauction;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Service
public class CustomOAuth2UserService implements OAuth2UserService<OidcUserRequest,OidcUser> {
	
	@Autowired
	public UserRepository userRepository;

	private HttpSession httpSession;
	public CustomOAuth2UserService(HttpSession httpSession) {
	        this.httpSession = httpSession;
	}

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUserService delegate = new OidcUserService();
		OidcUser oidcUser = delegate.loadUser(userRequest);
		String battletag="";
		
		
		
		
		httpSession.setAttribute("battletag", (String) oidcUser.getAttributes().get("battle_tag"));
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		DefaultOidcUser returnoidcuser = null;
		try {
			battletag = (String) oidcUser.getAttributes().get("battle_tag");
			if(exists(battletag))
			{
				roles.add(new SimpleGrantedAuthority("ROLE_USER"));
			}
			else
			{
				roles.add(new SimpleGrantedAuthority("ROLE_BANNED"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println(e.getMessage());
		}
		returnoidcuser = new DefaultOidcUser(roles, oidcUser.getIdToken(),oidcUser.getUserInfo());
		//System.out.println("customoauth2userservice+loaduser() "+battletag);
		
		
		return returnoidcuser;
	}

	private boolean exists(String battletag) {

		//차단 유저가 DB에 있으면 false
		return !userRepository.existsById(battletag);
	}


}