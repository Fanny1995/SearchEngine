package com.search_engine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.dbcp.dbcp2.PStmtKey;

import atg.taglib.json.util.HTTP;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/getParameters")
public class getParameters extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public getParameters() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("...");
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		
		System.out.println("___");
		String keyword = request.getParameter("keyword");
		String page = request.getParameter("page");
		String pattern = request.getParameter("pattern");
		JSONArray jsonArray = null;
		System.out.println(keyword + " " + page + " " + pattern);
		Search search = new Search();
		try {
			if (pattern == null)
				jsonArray = search.searchByRelevence(keyword);
			else
				jsonArray = search.searchByPattern(keyword, pattern);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("____");
		JSONObject result = new JSONObject();
		try {
			result.put("result", jsonArray);
		    result.put("time", 0);
		    result.put("pageCount", 0);
		    System.out.println(result.toString());
		    response.setContentType("text/html;charset=utf-8");
		    response.getWriter().write(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
		HttpClient client = HttpClients.createDefault();
		String url = "http://localhost:8080/SearchEngine/getParameters";
		URIBuilder builder = null;
		try {
			builder = new URIBuilder(url);
//			URLEncoder.encode("中国", "UTF-8")
			builder.setParameter("keyword", "中国").setParameter("page", "1").setParameter("pattern", "time");
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
//		catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		HttpPost post = null;
		try {
			System.out.println(builder.build());
			post = new HttpPost(builder.build());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			System.out.println(EntityUtils.toString(entity));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		PrintWriter writer = null;
//		try {
//			writer = new PrintWriter("/Users/wangshicheng/Desktop/test.txt", "UTF-8");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		writer.println("The first line");
//		writer.println("The secofdafdafnd line");
//		writer.close();
		System.out.println("**********");
		
//		PrintWriter writer = null;
//		try {
//			writer = new PrintWriter("/Users/fanny/Desktop/test.txt", "UTF-8");
//			writer.println("hello world");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			writer.close();
//		}
//		writer.println("keyword: " + URLDecoder.decode(request.getParameter("keyword"), "UTF-8"));
//		writer.println("page: " + request.getParameter("page"));
		
		
	}
}
