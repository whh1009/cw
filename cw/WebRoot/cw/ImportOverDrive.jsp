<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<title>${title }--OverDrive</title>
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
				<h3>提示：</h3>
				<p><strong>请注意模版导入名称规范一定是：</strong></p>
			</blockquote>
		</div>
		<div class="row">
			<div class="form-group">
	            <input id="file" name="file" type="file" class="file" data-preview-file-type="text">
	        </div>
        </div>
	</div>
	
	<script>
	
	$("#file").fileinput({
        language: "zh",
        uploadUrl: "${ctx }/import/uploadAppStoreExcel",
        maxFilesNum: 1,
        allowedFileExtensions : ["zip"],
    });
	$(function() {
		$("#file").on("fileuploaded", function(event, data, previewId, index) {
			$.ajax({
				url:"${ctx }/import/saveAppStoreBook",
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
	
	</script>
</body>
</html>
