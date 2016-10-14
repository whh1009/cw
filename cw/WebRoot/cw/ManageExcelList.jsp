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
					<th>来源</th>
					<th>原始文件名</th>
					<th>服务器名称</th>
					<th>时间</th>
					<th data-toggle='tooltip' data-placement='left' title='注意：点击删除时，会删除该excel中的全部数据'>操作</th>
				</tr>
			</thead>
			<tbody id="tbody"></tbody>
		</table>
		<div id="page"></div>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			//初始化tooltip
			$('[data-toggle="tooltip"]').tooltip();
			
			
			initSearch(1);
			
		});
		
		function initSearch(page) {
			$.ajax({
				url:"${ctx}/import/getExcelByPage",
				method:"POST",
				data:{page:page},
				beforeSend:function() {
					$("#tbody").showLoading();
				},
				success:function(data) {
					$("#tbody").hideLoading();
					var html = "";
					if(data) {
						for(var i=0;i<data.list.length;i++){
							html+="<tr>";
							if(data.list[i].type==1) {
								html+="<td>电子书支出</td>";
							} else if(data.list[i].type==2) {
								html+="<td>电子书收入</td>";
							} else {
								html+="<td>&nbsp;</td>";
							}
							html+="<td>"+data.list[i].upload_excel_name+"</td>";
							html+="<td>"+data.list[i].server_excel_name+"</td>";
							html+="<td>"+data.list[i].add_time+"</td>";
							html+="<td><a href='${ctx}/uploadFiles/"+data.list[i].server_excel_name+"' title='下载'><i class='glyphicon glyphicon-download-alt'></i></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"javascript:remove('"+data.list[i].type+"', '"+data.list[i].server_excel_name+"')\" data-toggle='tooltip' data-placement='left' title='删除，会删除该excel中的全部数据'><i class='glyphicon glyphicon-trash'></i></a></td>";
							html+="</tr>";
						}
						initPage(data.pageNumber, data.totalPage, data.totalRow);
					}
					$("#tbody").html(html);
				},
				error:function() {
					$("#tbody").hideLoading();
					$("#tbody").html("<tr><td colspan='2'><font color='red'>暂无数据</font></td></tr>");
				}
			});
		}
		
		//删除excel
		function remove(type, excelName) {
			if(confirm("确定要删除吗？")){
				$.post("${ctx}/import/removeExcel", {type:type, excelName:excelName}, function(data) {
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
		
		//初始化分页
	    function initPage(pageNumber, totalPage, totalRow) {
			var page = "<nav><ul class='pagination'><li><span>共 "+totalRow+" 条，"+totalPage+" 页</span></li>";
			if (pageNumber == 1) {
				page += "<li class='disabled'><a href='javascript:void();' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
			} else {
				page += "<li><a href='javascript:initSearch(1)' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
			}
			if (pageNumber <= 5) {
				var endPage = 10;
				if (endPage <= totalPage) {
					endPage = 10;
				} else {
					endPage = totalPage;
				}
				for (var i = 1; i <= endPage; i++) {
					if (i == pageNumber) {
						page += "<li class='active'><a href='javascript:initSearch(" + i + ")'>" + i + " <span class='sr-only'></span></a></li>";
					} else {
						page += "<li><a href='javascript:initSearch(" + i + ")'>" + i + "</a></li>";
					}
				}
			} else {
				var startPage = pageNumber - 5;
				var endPage = pageNumber + 5;
				if (endPage >= totalPage) {
					endPage = totalPage;
				}
				for (var i = startPage; i <= endPage; i++) {
					if (i == pageNumber) {
						page += "<li class='active'><a href='javascript:initSearch(" + i + ")'>" + i + " <span class='sr-only'></span></a></li>";
					} else {
						page += "<li><a href='javascript:initSearch(" + i + ")'>" + i + "</a></li>";
					}
				}
			}
			if (pageNumber == totalPage) {
				page += "<li class='disabled'><a href='javascript:void();' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
			} else {
				page += "<li><a href='javascript:initSearch(" + totalPage + ")' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
			}
			page += "</ul></nav>";
			$("#page").html(page);
		}
	</script>
</body>
</html>
