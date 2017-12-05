<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<form action = "get_res.jsp" method = "post">
	<input id = "keyword" name = "keyword" value = "中国">
	<input id = "page" name = "page" type = "hidden" value = "1">
	<input id = "search_button" name = "search_button" style = "color:green" value = "搜索" type = "submit">
	<!-- <input id = "pattern" name = "pattern"> -->
	<select name = "pattern">
	<option value = "hot" > 热度 </option>
	<option value = "time"  selected = "selected"> 时间 </option>
	</select>
	
	
</form>