<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%= request.getParameter("pattern") %>

<form action = "getParameters" method = "post">
<input name = "keyword" value = "中国">
<input name = "pattern" value = "time">
<input name = "page" value = "1">
<input name = "button" value = "提交" type = "submit">
</form>
</body>
</html>