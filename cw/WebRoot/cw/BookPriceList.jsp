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
<title>${title }--图书价格列表</title>
<link rel="shortcut icon" href="${ctx }/favicon.ico" />
<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<style>
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
	<jsp:include page="_header.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<div class="col-sm-3">
				<select class="form-control" id="saleTimeSel" onchange="changeSaleTime()">
					<option value="0">请选择月份</option>
				</select>
			</div>
			<div class="col-sm-3">
				<button class="btn btn-success" onclick="importData()">导出</button>
			</div>
		</div>
		<div class="row">
			<table class="table table-hover" id="tableContent">
				<thead></thead>
				<tbody></tbody>
			</table>
			<div id="page"></div>
		</div>
	</div>
	<script>
		var _page = 1;
		var _mySearchSql= "";
		$(function() {
			initSaleTime();
			initTableHeader();
			initTableBody();
		});
		
		function initSaleTime() {
			$.post("${ctx}/book/getDisTinctSaleTime", {}, function(data) {
				var saleTimeSel = "<option value='0'>请选择月份</option>";
				if(data) {
					for(var i=0;i<data.length;i++){
						saleTimeSel+="<option value='"+data[i].sale_time+"'>"+data[i].sale_time+"</option>";
					}
				}
				$("#saleTimeSel").html(saleTimeSel);
			});
		}
		
		function changeSaleTime() {
			_page = 1;
			var saleTime = $("#saleTimeSel").val();
			if(saleTime=="0"||saleTime=="") return;
			_mySearchSql = " and sale_time = "+saleTime;
			initSearch(1);
		}
		
		//初始化表格标题
		function initTableHeader() {
			var header = "<tr><th>ISBN</th><th>书名</th><th>责编</th><th>时间</th><th>总金额</th><th>总量</th><th>平台</th></tr>";
			$("#tableContent thead").html(header);
		}
		
		//初始化表格正文
		function initTableBody() {
			_page = 1;
			initSearch(1);
		}
		
		function initSearch(page) {
			$.ajax({
				url:"${ctx}/book/getBookPriceList",
				method:"POST",
				data:{page:_page, mySearchSql: _mySearchSql},
				beforeSend:function() {
					$("#tableContent").showLoading();
				},
				success:function(data) {
					$("#tableContent tbody").html("");
					$("#tableContent").hideLoading();
					if(data) {
						if(data.list.length>0) {
							var content= "";
							for(var i = 0; i<data.list.length; i++) {
								content+="<tr><td>"+data.list[i].book_isbn+"</td>";
								content+=    "<td>"+data.list[i].book_name+"</td>";
								content+=    "<td>"+data.list[i].book_author+"</td>";
								content+=    "<td>"+data.list[i].sale_time+"</td>";
								content+=    "<td>"+data.list[i].sale_total_price+"</td>";
								content+=    "<td>"+data.list[i].sale_total_count+"</td>";
								if(data.list[i].platform==1) {
									content+="<td>亚马逊美国</td>";
								} else if(data.list[i].platform==2) {
									content+="<td>亚马逊中国</td>";
								} else if(data.list[i].platform==3) {
									content+="<td>App Store</td>";
								}
								content+="</tr>";
							}
							$("#tableContent tbody").html(content);
							initPage(data.pageNumber, data.totalPage, data.totalRow);
						}
					}
				},
				error:function() {
					$("#tableContent").hideLoading();
					
				}
			});
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
			
		//导出
		function importData() {
			
		}
	</script>
</body>
</html>
