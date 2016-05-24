<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<title>${title }--AppStore</title>
<link rel="shortcut icon" href="${ctx }/favicon.ico" />
<link href="${ctx }/css/fileinput.css" rel="stylesheet"/>
<link href="${ctx }/css/showLoading.css" rel="stylesheet">

<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="${ctx }/js/fileinput.js" type="text/javascript"></script>
<script src="${ctx }/js/fileinput_locale_zh.js" type="text/javascript"></script>
<script src="${ctx }/js/jquery.showLoading.min.js"></script>
</head>

<body>
	<jsp:include page="_header.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<blockquote>
				<h3>提示：</h3>
				<p>请上传app stroe官方下载的账单zip</p>
			</blockquote>
		</div>
		<div class="row">
			<form class="form-group" action="${ctx }/import/uploadExcel" method="post" enctype="multipart/form-data" onsubmit="return checkAppStoreFile()">
	            <input id="file" name="file" type="file" class="file" data-preview-file-type="text">
	            <input type="hidden" name="t" value="4" />
	            <br />
	            <input type="submit" class="btn btn-success" value="上传" />
	            <input type="reset" class="btn" value="取消" />
	        </form>
        </div>
	</div>
	<script type="text/javascript">
	
	function checkAppStoreFile() {
		 var file = $("#file").val();
		 if(file==null||file==undefined||file=="") {
			 alert("请至少选择一个zip文件");
			 return false;
		 } else {
			 var ext = file.substring(file.lastIndexOf(".")+1, file.length).toLowerCase();
			 if(ext=="zip") {
				 return true;
			 } else {
				 alert("亲，请上传一个zip文件哦");
				 return false;
			 }
		 }
	 }
	</script>
</body>
</html>
