<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<title>${title }</title>
<link rel="shortcut icon" href="${ctx }/favicon.ico" />
<link href="${ctx }/css/fileinput.css" rel="stylesheet" />
<link href="${ctx }/css/showLoading.css" rel="stylesheet">

<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="${ctx }/js/fileinput.js" type="text/javascript"></script>
<script src="${ctx }/js/fileinput_locale_zh.js" type="text/javascript"></script>
<script src="${ctx }/js/jquery.showLoading.min.js"></script>

<link href="${ctx }/css/showLoading.css" rel="stylesheet">
<script src="${ctx }/js/jquery.showLoading.min.js"></script>
</head>

<body>
	<jsp:include page="_header.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<form method="post" action="${ctx }/import/test" enctype="multipart/form-data" onsubmit="return checkFile()">
				<input id="file" name="file" type="file" class="file"> <br>
				<button type="submit" class="btn btn-primary">上传</button>
				<button type="reset" class="btn btn-default">取消</button>
			</form>
		</div>
	</div>

	<script>
	function checkFile() {
		var file = $("#file").val();
		if(file==null||file==undefined||file=="") {
			alert("请选择文件");
			$("#file").focus();
			return false;
		} else {
			if(file.length>5&&file.substring(file.length-5)==".xlsx") {
				return true;
			} else {
				alert("请上传一个正确的excel（xlsx）文件");
				return false;
			}
		}
	}
	</script>
</body>
</html>
