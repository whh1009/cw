<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Access-Control-Allow-Origin" content="*">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-touch-fullscreen" content="YES">
<meta content="telephone=no" name="format-detection">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
<link rel="shortcut icon" href="${ctx }/images/favicon.ico"/>
<link rel="bookmark" href="${ctx }/favicon.ico"/>
<title>${title }--登录</title>
<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<link href="${ctx}/css/bootstrap.min.css" rel="stylesheet">
<link href="${ctx}/css/signin.css" rel="stylesheet">
<style>
.msg{
	color:red;
	clear:both;
}
.box{
	margin:0 auto;
}
</style>
<script>
	$(function() {
		$(".msg").hide();
	});

	function validateForm() {
		var userName=$("input[name='userModel.user_name']").val().trim();
		var pwd=$("input[name='userModel.user_password']").val().trim();
		if(userName==""){
			$(".msg").html("Enter you name");
			$("input[name='userModel.user_name']").focus();
			return false;
		}
		if(pwd=="") {
			$(".msg").html("Enter you password");
			$("input[name='userModel.user_password']").focus();
			return false;
		}
		return true;
	}
	
	function login() {
		var userName = $("#userName").val().trim();
		var pwd = $("#pwd").val().trim();
		if (userName == "") {
			$(".msg").html("请输入用户名");
			$(".msg").show();
			$("#userName").focus();
			return;
		} else {
			$(".msg").hide();
		}
		if (pwd == "") {
			$(".msg").html("请输入密码");
			$(".msg").show();
			$("#pwd").focus();
			return;
		} else {
			$(".msg").hide();
		}
		$.ajax({
			url : "${ctx }/user/login",
			type : "post",
			data : {
				userName : userName,
				userPassword : pwd
			},
			dataType : "json",
			success : function(data) {
				console.log(data);
				if (data == "0") {
					$(".msg").html("对不起，用户名或密码有误");
					$(".msg").show();
				} else if(data=="2"){
					$(".msg").html("对不起，你的账号已被禁用");
					$(".msg").show();
				} else if(data=="-1"){
					$(".msg").html("登录异常");
					$(".msg").show();
				} else {
					window.location.href = "${ctx}/";
				}
			}
		});
	}
	
	function enterPress(event) {
		var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		if(keyCode == 13){ 
			login();
		} 
	}
</script>

</head>

<body class="login-bg">
	<div class="container">
		<div class="row">
			<div class="login-wrapper">
				<div class="box">
					<div class="content-wrap">
						<h3 style=""><img src="${ctx }/images/logo.png" style="width:50px" />&nbsp;&nbsp;五洲传播出版社</h3>
						<br />
						<div class="col-sm-12">
							<input type="text" name="userModel.user_name" id="userName" class="form-control" placeholder="user name" maxlength="30" onkeydown="enterPress(event)" />
						</div>
						<div class="col-sm-12">
							<input type="password" name="userModel.user_password" id="pwd" class="form-control" placeholder="password"  maxlength="30" onkeydown="enterPress(event)" />
						</div>
						<p class="msg"></p>
						<button class="btn btn-primary login" type="button" onclick="login()">登录</button>
					</div>
				</div>
				<div class="col-sm-12 no-account">
					<p><a href="http://192.168.1.199/wuzhou" target="_blank"><span class="glyphicon glyphicon-hand-right"></span> 数据采集系统</a></p>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
