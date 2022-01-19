package com.blizzard.d2ritemauction;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Simple controller that adds some information about the OAuth user and client to the model before rendering the
 * index page. Requires a template named {@code index.html} in the templates folder.
 *
 * @author Tyler Gregory
 * @since 6/16/2021
 */
@Controller
public class IndexController {

	
	@GetMapping("/")
	public ModelAndView index(
			HttpServletRequest  request,
			HttpServletResponse response

	) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		HttpSession session = request.getSession();
		String battletag = (String) session.getAttribute("battletag");
		
		if(battletag!=null)
		{
			mav.addObject("battletag", battletag);
			mav.addObject("logintrigger", "logout");
		}
		else
		{
			
			mav.addObject("logintrigger", "login");
		}
		
		
		return mav;
	}
	
	@GetMapping("/search")
	public ModelAndView search(
			
			HttpServletRequest  request,
			HttpServletResponse response
			)
	{
		ModelAndView mav = new ModelAndView();
		mav.setViewName("search");
		HttpSession session = request.getSession();
		String battletag = (String) session.getAttribute("battletag");
		
		if(battletag!=null)
		{
			mav.addObject("battletag", battletag);
			mav.addObject("logintrigger", "logout");
		}
		else
		{
			
			mav.addObject("logintrigger", "login");
		}
		return mav;
		
	}
	
	@GetMapping("/insert")
	public ModelAndView insert(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal OAuth2User oauth2User)
	{
		ModelAndView mav = new ModelAndView();
		mav.addObject("battletag",oauth2User.getAttributes().get("battle_tag"));
		mav.setViewName("insert");
		
		
		return mav;
	}
	@GetMapping("/mypage")
	public ModelAndView mypage(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal OAuth2User oauth2User)
	{
		ModelAndView mav = new ModelAndView();
		mav.setViewName("mypage");
		mav.addObject("battletag",oauth2User.getAttributes().get("battle_tag"));
		
		return mav;
		
	}

 

}
