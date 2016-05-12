<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<title>${title }--图书列表</title>
<link rel="shortcut icon" href="${ctx }/favicon.ico" />
<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<style>
.cen {
	text-align: center;
}

table {
	font-size: 90%;
}

.input-group,.col-lg-3 {
	margin-top: 1em;
}
</style>


<link href="${ctx }/css/showLoading.css" rel="stylesheet">
<script src="${ctx }/js/jquery.showLoading.min.js"></script>

<script type="text/javascript">
	
</script>
</head>

<body>
	
	<script>
	$(function() {
		
	})
	</script>
</body>
</html>
