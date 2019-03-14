<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

	Object msgObj = request.getSession().getAttribute("error");
	String msg = "";
	if (msgObj != null) {
		request.getSession().removeAttribute("error");
		msg = msgObj.toString();
	}
%>
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>
<link type="text/css" rel="stylesheet" href="<%=basePath %>css/common.css">
<script src="<%=basePath %>js/jquery1.9.1.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
<div class="container">
	<form action="account/register" method="post">
	<div class="row">
	    <span>用户名：</span>  
	    <input type="text" name="user_name"> 
	</div>
	<div class="row">
	    <span>手机号：</span>  
	    <input type="text" name="phone"> 
	</div>
	<div class="row">
	    <span>&nbsp;&nbsp;&nbsp;&nbsp;密码：</span>  
	    <input type="password" name="password">
	</div>
	<div class="row">
		<p class="error"><%=msg %></p>
	    <button type="submit">注册</button>
	</div>
	</form>
</div>
</body>
</html>