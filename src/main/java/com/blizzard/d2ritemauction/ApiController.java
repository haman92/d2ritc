package com.blizzard.d2ritemauction;

import java.io.BufferedReader;
import java.util.Map;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.google.gson.Gson;

@RestController
public class ApiController {

	@Autowired
	DynamoDBService ddbs;

	
	@RequestMapping(method = RequestMethod.POST,value = "/inputitem")   // localhost:8080/inputitem
	public boolean inputitem(String param)
	{
		//System.out.println(param);
		
		Gson gson = new Gson();
		HashMap<String,String> map = new HashMap<String,String>();
		map = (HashMap<String, String>)gson.fromJson(param,map.getClass());
		
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss",currentLocale);
		String time=formatter.format(today);
		map.put("InputTime", time);
		//System.out.println(time);
		if(ddbs.createitem(map))
			return true;
		else
			return false;
		
		
	}

	

	@RequestMapping(method = RequestMethod.GET, value = "/getitemfilter")   // localhost:8080/getRequest
	public String getitemfilter(String param)
	{
		String ret="";
		InputStream in = null;
		BufferedReader br;
		String line;
		URL url;
		//System.out.println(param);
		try {
			//d18zq3onvlr5bf.cloudfront.net/item_filter/21335.json
			url = new URL("https://d18zq3onvlr5bf.cloudfront.net/item_filter/"+param+".json");
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			con.setRequestMethod("GET");   
			con.setConnectTimeout(2000);  
			con.setReadTimeout(2000); 
			int responseCode = con.getResponseCode();

			if (responseCode == HttpsURLConnection.HTTP_OK) { 
				in = con.getInputStream(); } else { 
					in = con.getErrorStream(); 
				} 
			br = new BufferedReader(new InputStreamReader(in)); 
			while ((line = br.readLine()) != null) 
			{ 
				ret = ret+line; 
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} 

		//System.out.println(ret);

		return ret;
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/getddbitem")   // localhost:8080/getRequest
	public String getddbitem(String param) throws UnsupportedEncodingException{

		//System.out.println("param"+param);
		Iterator<Item> iterator = ddbs.retrieveitem(param,50);

		String ret = "[\n";
		while (iterator.hasNext()) {

			String temp = iterator.next().toJSONPretty();
			if(iterator.hasNext())
			{
				temp+=",\n";
			}
			ret=ret+temp;


		}
		if(ret.equals("[\n"))
			ret += "{\"none\":\"1\"}";
		ret=ret+"\n]";
		//System.out.println("ret");
		
		ret = new String(ret.getBytes("UTF-8"),"UTF-8");
		//System.out.println(ret);
		return ret;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getbtddbitem")   // localhost:8080/getRequest
	public String getbtddbitem(String param) throws UnsupportedEncodingException{

		
		//System.out.println("param"+param);
		
		param =param.replace('%','#');
		Iterator<Item> iterator = ddbs.retrieveitem(param,10000);

		String ret = "[\n";
		while (iterator.hasNext()) {

			String temp = iterator.next().toJSONPretty();
			if(iterator.hasNext())
			{
				temp+=",\n";
			}
			ret=ret+temp;


		}
		if(ret.equals("[\n"))
			ret += "{\"none\":\"1\"}";
		ret=ret+"\n]";
		//System.out.println("ret");
		
		ret = new String(ret.getBytes("UTF-8"),"UTF-8");
		//System.out.println(ret);
		return ret;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/updateddbitem")   // localhost:8080/getRequest
	public String updateddbitem(String param)
	{
		String ret="{}";
		//System.out.println(param);
		param =param.replace('%','#');
		Gson gson = new Gson();
		HashMap<String,String> map = new HashMap<String,String>();
		map = (HashMap<String, String>)gson.fromJson(param,map.getClass());
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss",currentLocale);
		String time=formatter.format(today);
		map.put("updateInputTime", time);
		
		ddbs.updateitem(map);
		return ret;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/deleteddbitem")   // localhost:8080/getRequest
	public String deleteddbitem(String param)
	{
		String ret="{}";
		param =param.replace('%','#');
		Gson gson = new Gson();
		HashMap<String,String> map = new HashMap<String,String>();
		map = (HashMap<String, String>)gson.fromJson(param,map.getClass());
		
		ddbs.deleteitem(map);
		return ret;
	}
}
