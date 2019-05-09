<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<meta charset="UTF-8">
<title>Index</title>
<link type="text/css" rel="stylesheet" href="<%=basePath %>css/common.css">
<style type="text/css">
	.container img {
		width: 200px;
	    margin: 0 auto;
	    display: block;
	} 
</style>
<script src="<%=basePath %>js/jquery1.9.1.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
<div class="container">
	<img src="user/icon/downloadbyim?session=zutM4HuDH3httArf3EdSv95fwfZzc4wa" />
</div>
</body>
</html>