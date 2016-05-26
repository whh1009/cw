<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<link rel="shortcut icon" href="${ctx }/favicon.ico" />
<title>${title }</title>
<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<link href="${ctx }/css/showLoading.css" rel="stylesheet">
	<script src="${ctx }/js/jquery.showLoading.min.js"></script>
<style>
table{
	font-size:12px;
}
</style>

</head>
<body>

	<jsp:include page="_header.jsp"></jsp:include>
	<div class="container-fluid">
		<div class="row">
			<table id="table" class="table order-column">
			<thead>
				<tr>
					<th>上传平台</th>
					<th>原始文件名</th>
					<th>服务器名称</th>
					<th>时间</th>
					<th data-toggle='tooltip' data-placement='left' title='注意：点击删除时，会删除该excel中的全部数据'>操作</th>
				</tr>
			</thead>
			<tbody id="tbody"></tbody>
		</table>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			//初始化tooltip
			$('[data-toggle="tooltip"]').tooltip();
			
			
			$.ajax({
				url:"${ctx}/import/getAllExcelList",
				method:"POST",
				beforeSend:function() {
					$("#tbody").showLoading();
				},
				success:function(data) {
					$("#tbody").hideLoading();
					var html = "";
					for(var i=0;i<data.length;i++){
						html+="<tr>";
						if(data[i].type==1) {
							html+="<td>基本表</td>";
						} else if(data[i].type==2) {
							html+="<td>亚马逊美国</td>";
						} else if(data[i].type==3) {
							html+="<td>亚马逊中国</td>";
						} else if(data[i].type==4) {
							html+="<td>App Store</td>";
						} else if(data[i].type==5) {
							html+="<td>Over Drive</td>";
						} else if(data[i].type==6){
							html+="<td>That's Books</td>";
						} else {
							html+="<td>&nbsp;</td>";
						}
						html+="<td>"+data[i].upload_excel_name+"</td>";
						html+="<td>"+data[i].server_excel_name+"</td>";
						html+="<td>"+data[i].add_time+"</td>";
						html+="<td><a href='${ctx}/uploadFiles/"+data[i].server_excel_name+"' title='下载'><i class='glyphicon glyphicon-download-alt'></i></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"javascript:remove('"+data[i].server_excel_name+"')\" data-toggle='tooltip' data-placement='left' title='删除，会删除该excel中的全部数据'><i class='glyphicon glyphicon-trash'></i></a></td>";
						html+="</tr>";
					}
					$("#tbody").html(html);
				},
				error:function() {
					$("#tbody").hideLoading();
					$("#tbody").html("<tr><td colspan='2'><font color='red'>暂无数据</font></td></tr>");
				}
			});
			
		});
		
		//删除excel
		function remove(excelName) {
			if(confirm("确定要删除吗？")){
				$.post("${ctx}/import/removeExcel", {excelName:excelName}, function(data) {
					if(data=="1") {
						alert("删除成功");
						window.location.href=window.location.href;
					} else if(data=="0") {
						alert("未获取到文件名");
					} else {
						alert("删除失败");
					}
				});
			}
		}
	</script>
</body>
</html>
