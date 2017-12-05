package com.search_engine;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class GetSearchRes {
	static HttpClient client;
	static String url = "http://localhost:8080/SearchEngine/getParameters"; //TODO add url
	static JSONObject search_res = null; 
	static JSONArray search_array = null;
	static int array_len = 0;
	static int pageCount = 0;
	public static void getSearchRes(String keyword, int page, String pattern) throws ParseException, IOException {
		client = HttpClients.createDefault();
		try {
			URIBuilder builder = new URIBuilder(url);
			System.out.println(keyword + " " + page + " " + pattern);
			builder.setParameter("keyword", keyword).setParameter("page", Integer.toString(page)).setParameter("pattern", pattern);
			HttpPost post = new HttpPost(builder.build());
			HttpResponse response = client.execute(post);
			String body = EntityUtils.toString(response.getEntity());
			System.out.println("+++");
			System.out.println(body);
			search_res = new JSONObject(body);
			assert(search_res.length() == 3);
			search_array = search_res.getJSONArray("result");
			array_len = search_array.length();
			pageCount = Integer.parseInt(search_res.getString("pageCount"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int getSearchLen() { 
		return array_len;
	}
	public static double getTimeConsume() {
		double time_consume = 0.0;
		if (search_res != null) {
			String temp;
			try {
				temp = search_res.getString("time");
				time_consume = Double.parseDouble(temp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return time_consume;
	}
	public static int getPageIndex() {
		int page_index = 0;
		if (search_res != null) {
			try {
				String temp = search_res.getString("pageCount");
				page_index = Integer.parseInt(temp);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return page_index;
	}
	
	public static JSONObject getOneItem(int index) {
		if (index >= array_len) return null;
		try {
			return (JSONObject) search_array.get(index);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getPageCount() {
		return pageCount;
	}
	public static void main(String[] args) throws ParseException, IOException {
		GetSearchRes.getSearchRes("中国", 1, "time");
		System.out.println(GetSearchRes.getSearchLen());
		System.out.println(GetSearchRes.getTimeConsume());
		System.out.println(GetSearchRes.getPageIndex());
		
		
	}
}
