<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<title>${title }--批量生成书号</title>
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
				<p><small>请上传正确的模板，若不清楚模板格式可以<a href="${ctx }/temp/基本表导入模板.xlsx">点我下载</a></small></p>
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
        uploadUrl: "${ctx }/import/uploadBaseBookExcel",
        maxFilesNum: 1,
        allowedFileExtensions : ["xlsx"],
    });
	$(function() {
		$("#file").on("fileuploaded", function(event, data, previewId, index) {
			$.ajax({
				url:"${ctx }/import/saveBaseBookExcel",
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
