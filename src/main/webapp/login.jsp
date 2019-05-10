<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="sf"  uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link type="text/css" rel="stylesheet" href="<%=basePath %>css/common.css">
<script src="<%=basePath %>js/jquery1.9.1.js"></script>
<script src="<%=basePath %>js/base64.js"></script>
<script src="<%=basePath %>js/jsencrypt.min.js"></script>
<script type="text/javascript">
	function formSubmit() {
		var account = $("#dis-account").val();
		if (!account) {
			alert("账号不能为空！");
			return false;
		}
		var password = $("#dis-password").val();
		if (!password) {
			alert("密码不能为空！");
			return false;
		}
		
		var encrypt = new JSEncrypt();
		encrypt.setPublicKey(JSSign.APIPublicKey);
		var url = "account=" + account + "&password=" + password;
		var encryptData = encrypt.encrypt(url);
		$("#encryp_data").val(encryptData);
		return true;
	}
</script>
</head>
<body>
<div class="container">
	<div class="row">
	    <span>账号：</span>  
	    <input type="text" name="dis-account" id="dis-account" placeholder="登录名/手机号/邮箱"> 
	</div>
	<div class="row">
	    <span>密码：</span>  
	    <input type="password" name="dis-password" id="dis-password">
	</div>
	<div class="row">
		<form action="account/login" method="post" onsubmit="return formSubmit()">
			<input id="encryp_data" name="encryp_data" type="hidden" value="" >
	    	<button type="submit">登录</button>
	    </form>
	</div>
</div>
</body>
</html>