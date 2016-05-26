<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<title>${title }--亚马逊美国</title>
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
	<div class="container-fluid">
		<div class="row">
			<blockquote>
				<h3>提示</h3>
				<p>亚马逊美国模版要求：</p>
				<ul>
					<li class="fontRed">第一个Sheet是书单内容</li>
					<li class="fontRed">Sheet名格式为CNIXX 20160301 - 20160401</li>
				</ul>
				<small><a href="${ctx }/temp/CNICN_DigitalEBooksPaymentReport_20160229-20160331.xls">下载模版</a></small>
			</blockquote>
		</div>
		<div class="row">
			<form class="form-group" action="${ctx }/import/uploadExcel" method="post" enctype="multipart/form-data" onsubmit="return checkFile()">
	            <input id="file" name="file" type="file" class="file" data-preview-file-type="text">
	            <input type="hidden" name="t" value="3" />
	            <br />
	            <input type="submit" class="btn btn-success" value="上传" />
	            <input type="reset" class="btn" value="取消" />
	        </form>
        </div>
	</div>
</body>
</html>
