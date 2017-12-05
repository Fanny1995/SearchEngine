<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "org.json.JSONArray" %>
<%@ page import = "com.search_engine.GetSearchRes" %>
<%@ page errorPage = "error_page.jsp" %>
<%@ page import = "org.json.JSONObject" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<% request.setCharacterEncoding("utf-8"); %>
<title><%= request.getParameter("keyword") %></title>
</head>
<body>
<div>

<%!
String keyword;
int page_number;
int page_count;
String pattern;
%>
<%
keyword = request.getParameter("keyword"); 
String page_number_string = request.getParameter("page");
if (keyword == null || keyword.equals("")) { %>
<jsp:forward page = "search_form.jsp"/>
<%
}
pattern = request.getParameter("pattern");
System.out.println(pattern);
%>

<jsp:include page = "search_form.jsp"/>

<%
try {
	page_number = Integer.parseInt(page_number_string); 
} catch (Exception e) {  //for page_number_string = "" or null or not a number, all will cause an exception
	e.printStackTrace();
	throw new RuntimeException("page number has an error!");
}
GetSearchRes.getSearchRes(keyword, page_number, pattern);
page_count = GetSearchRes.getPageCount();
int res_len = GetSearchRes.getSearchLen();
if (res_len == 0) {  %>
	<jsp:forward page = "none_res.jsp"/>
<%
}
%>
<p> <span>搜索结果:<%= res_len %>	条</span>
<span>搜索时间: <%= GetSearchRes.getTimeConsume() %>s</span>
</p>TODO??
<%
for (int i = 0; i < res_len; i++) {
	JSONObject obj = GetSearchRes.getOneItem(i);
%>
<p>
<a href = "<%= obj.getString("url") %>"> <%= obj.getString("title") %> </a>
</p>
<div>
<p>
<%= obj.getString("contents") %>
</p>
</div>
<p>
<%= obj.getString("add_time") %>
</p>
<br/> <br/>
<%	
}
%>

<div>

<%
for (int i = 1; i <= page_count; i++) {
	if (page_number == i) { %>
	<strong>
	<%= i %>
	</strong>
	<%
	} else { %>
		<form action  = "get_res.jsp" method = post>
		<input id = "keyword" name = "keyword" value = "<%= keyword %>" type = "hidden">
		<input id = "page" name = "page" value = "<%= i %>" type = "hidden">
		<input id = "pattern" name = "pattern" value = "<%= pattern %>" type = "hidden">
		<input id = "submit" name = "submit" value = "<%= i %>" type = "submit">
		</form>
	<%}
}
%>
</div>
</div>
</body>
</html>