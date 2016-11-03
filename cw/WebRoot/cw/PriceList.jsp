<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<title>${title }--排行榜</title>
<link rel="shortcut icon" href="${ctx }/favicon.ico" />
<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<style>
table {
	font-size: 90%;
}

.form-group {
	margin-right: 3em;
}
</style>

<link href="${ctx }/css/showLoading.css" rel="stylesheet">
<script src="${ctx }/js/jquery.showLoading.min.js"></script>

<script type="text/javascript">
	
</script>
</head>

<body>
	<jsp:include page="_header.jsp"></jsp:include>
	<div class="container-fluid">
		<div class="row">
			<div class="form-inline">
				<div class="form-group">
					<label>年份：</label>
					<select class="form-control" id="yearSel">
						<option value="0">请选年份</option>
					</select>
				</div>
				<div class="form-group">
					<label>时间：</label>
					<select class="form-control" id="monthSel">
						<option value="0">请选择月份</option>
					</select>
				</div>
				<button class="btn btn-info">查询</button>
			</div>
		</div>
		<div class="row">
			<table class="table table-hover" id="tableContent">
				<thead><th>书号</th><th>书名</th><th>销量</th><th>人民币</th><th>美元</th></thead>
				<tbody></tbody>
			</table>
		</div>
	</div>
	<script>
	var year = "2016";
	var month = "04";
	$(function() {
		initSearch();
		var years = "${years}";
		
	});
	
	function initSearch() {
		$.ajax({
			url:"${ctx}/incoming/getPriceList",
			method:"POST",
			data:{year:year, month:month},
			beforeSend:function() {
				$("#tableContent").showLoading();
			},
			success:function(data) {
				$("#tableContent tbody").html("");
				$("#tableContent").hideLoading();
				if(data) {
					var content= "";
					for(var i = 0; i<data.length; i++) {
						content+="<tr><td>"+data[i].bookNum+"</td>";
						content+=    "<td>"+data[i].bookName+"</td>";
						content+=    "<td>"+data[i].totalCount+"</td>";
						content+=    "<td>"+data[i].totalRmb+"</td>";
						content+=    "<td>"+data[i].totalDollar+"</td>";
						content+="</tr>";
					}
					$("#tableContent tbody").html(content);
				} else {
					$("#tableContent tbody").html("<tr><td><font color='red'>暂无数据</font></td></tr>");
					$("#page").html("");
				}
			},
			error:function() {
				$("#tableContent").hideLoading();
				
			}
		});
	}
	
			
	</script>
</body>
</html>
