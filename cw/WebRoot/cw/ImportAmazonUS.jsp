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
	<div class="container">
		<div class="row">
			<blockquote>
				<h3>提示：</h3>
				<p><strong>请注意亚马逊美国模版导入名称规范一定是：CNICN_DigitalEBooksPaymentReport_20130131-20130228</strong><small><a href="${ctx }/temp/CNICN_DigitalEBooksPaymentReport.xls">点我下载模版</a></small></p>
			</blockquote>
		</div>
		<div class="row">
			<form class="form-group" action="${ctx }/import/uploadAmazonUSBookExcel" method="post" enctype="multipart/form-data" onsubmit="return checkFile()">
	            <input id="file" name="file" type="file" class="file" data-preview-file-type="text">
	            <br />
	            <input type="submit" class="btn btn-success" value="上传" />
	            <input type="reset" class="btn" value="取消" />
	        </form>
        </div>
	</div>
	
	<script>
	function checkFile() {
		 var file = $("#file").val();
		 if(file==null||file==undefined||file=="") {
			 alert("请至少选择一个excel");
			 return false;
		 } else {
			 var ext = file.substring(file.lastIndexOf(".")+1, file.length).toLowerCase();
			 if(ext=="xlsx"||ext=="xls") {
				 return true;
			 } else {
				 alert("亲，只能上传07版本以上excel哦");
				 return false;
			 }
		 }
	 }
	$(function() {
		$("#file").fileinput({
	        language: "zh",
	        maxFilesNum: 1,
	        allowedFileExtensions : ["xlsx","xls"],
	    });
	});
	
	/*
	$(function() {
		$("#file").on("fileuploaded", function(event, data, previewId, index) {
			$.ajax({
				url:"${ctx }/import/saveAmazonUSBookExcel",
				type:"post",
				dataType:"json",
				beforeSend: function () {
					$("body").showLoading();
				},
				success:function(json) {
					$("body").hideLoading();
					if(json.status) {
						alert("上传成功，"+json.message);
					} else {
						alert("上传失败，失败原因："+json.message);
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown) {
					console.log(errorThrown);
					$("body").hideLoading();
				}
			});
		});
	});
	*/
	</script>
</body>
</html>
