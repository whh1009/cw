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
<title>${title }--图书收入列表</title>
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
					<label>平台：</label>
					<select class="form-control" id="platformSel">
						<option value="0">请选择平台</option>
					</select>
				</div>
				<div class="form-group">
					<label>时间：</label>
					<select class="form-control" id="saleTimeSel">
						<option value="0">请选择月份</option>
					</select>
				</div>
				<div class="form-group">
					<label>语种：</label>
					<select class="form-control" id="bookLanSel">
						<option value="0">请选择语种</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-inline">
				<div class="form-group">
					<input type="text" class="form-control" id="bookNumTxt" placeholder="请输入书号">
				</div>
				<div class="form-group">
					<input type="text" class="form-control" id="bookNameTxt" placeholder="请输入书名">
				</div>
				<div class="form-group">
					<input type="text" class="form-control" id="bookAuthorTxt" placeholder="请输入作者">
				</div>
				
				<div class="form-group">
					<button class="btn btn-success" onclick="initSearch(1)"><i class="glyphicon glyphicon-search"></i>&nbsp;检索</button>
				</div>
				<div class="form-group">
					<button class="btn btn-info" onclick="importData()"><i class="glyphicon glyphicon-import"></i>&nbsp;导出</button>
				</div>
			</div>
		</div>
		<div class="row">
			<div id="priceCountDiv"></div>
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
			initPlatform();
			initSaleTime();
			initBookLan();
			initTableHeader();
			initTableBody();
		});
		
		function mySearchCon() {
			_mySearchSql="";
			var saleTime = $("#saleTimeSel").val();
			var platform = $("#platformSel").val();
			var bookNum = $("#bookNumTxt").val();
			var bookName = $("#bookNameTxt").val();
			var bookAuthor = $("#bookAuthorTxt").val();
			var bookLan = $("#bookLanSel").val();
			if(saleTime!="0"&&saleTime!="") {
				_mySearchSql += " and sale_time = '"+saleTime+"'";
			}
			if(platform!="0"&&platform!="") {
				_mySearchSql += " and platform = '" + platform+"'";
			}
			if(bookName!="") {
				_mySearchSql+=" and book_name like '%"+bookName+"%'";
			}
			if(bookNum!="") {
				_mySearchSql+=" and book_num like '%"+bookNum+"%'";
			}
			
			if(bookAuthor!="") {
				_mySearchSql+=" and book_author like '%"+bookAuthor+"%'";
			}
			
			if(bookLan!="0"&&bookLan!="") {
				_mySearchSql+=" and book_lan = '"+bookLan+"'";
			}
		}
		
		//初始化第三方平台
		function initPlatform() {
			$.post("${ctx}/incoming/getDistinctPlatform", {}, function(data) {
				var platformSel = "<option value='0'>请选择平台</option>";
				if(data) {
					for(var i=0;i<data.length;i++){
						platformSel+="<option value='"+data[i].platform+"'>"+data[i].platform+"</option>";
					}
				}
				$("#platformSel").html(platformSel);
			});
		}
		
		//初始化时间
		function initSaleTime() {
			$.post("${ctx}/incoming/getDistinctSaleTime", {}, function(data) {
				var saleTimeSel = "<option value='0'>请选择月份</option>";
				if(data) {
					for(var i=0;i<data.length;i++){
						saleTimeSel+="<option value='"+data[i].sale_time+"'>"+data[i].sale_time+"</option>";
					}
				}
				$("#saleTimeSel").html(saleTimeSel);
			});
		}
		
		function initBookLan() {
			$.post("${ctx}/incoming/getDistinctBookLan", {}, function(data){
				var bookLanSel = "<option value='0'>请选择语种</option>";
				if(data) {
					for(var i=0; i<data.length; i++) {
						bookLanSel += "<option value='"+data[i].book_lan+"'>"+data[i].book_lan+"</option>";
					}
				}
				$("#bookLanSel").html(bookLanSel);
			});
		}
		
		function initPriceCount() {
			$.post("${ctx}/incoming/getBookPriceCount", {mySearchSql:_mySearchSql}, function(data){
				$("#priceCountDiv").html("<div class='alert alert-info' role='alert'><strong>汇总</strong>&nbsp;&nbsp;&nbsp;&nbsp;销售册数："+data.count+"&nbsp;&nbsp;&nbsp;回款人民币：￥"+data.rmb+"&nbsp;&nbsp;&nbsp;回款美元：$"+data.dollar+"</div>");
			});
		}
		
		//初始化表格标题
		function initTableHeader() {
			var header = "<tr><th>账单平台</th><th>账单日期</th><th>书号</th><th>书名</th><th>作者</th><th>文种</th><th>销售册数</th><th>原价</th><th>折扣率</th><th>回款人民币</th><th>回款美元</th><th>备注</th></tr>";
			$("#tableContent thead").html(header);
		}
		
		//初始化表格正文
		function initTableBody() {
			initSearch(1);
		}
		
		function initSearch(page) {
			mySearchCon();
			$.ajax({
				url:"${ctx}/incoming/getIncomingByPage",
				method:"POST",
				data:{page:page, mySearchSql: _mySearchSql},
				beforeSend:function() {
					$("#tableContent").showLoading();
				},
				success:function(data) {
					$("#tableContent tbody").html("");
					$("#tableContent").hideLoading();
					if(data) {
						if(data.out.list&&data.out.totalRow>0) {
							var content= "";
							for(var i = 0; i<data.out.list.length; i++) {
								content+="<tr><td>"+data.out.list[i].platform+"</td>";
								content+=    "<td>"+data.out.list[i].sale_time+"</td>";
								content+=    "<td>"+data.out.list[i].book_num+"</td>";
								content+=    "<td>"+data.out.list[i].book_name+"</td>";
								content+=    "<td>"+data.out.list[i].book_author+"</td>";
								content+=    "<td>"+data.out.list[i].book_lan+"</td>";
								content+=    "<td>"+data.out.list[i].sale_count+"</td>";
								content+=    "<td>"+data.out.list[i].discount+"</td>";
								content+=    "<td>"+data.out.list[i].zkl+"</td>";
								content+=    "<td>"+data.out.list[i].sale_rmb+"</td>";
								content+=    "<td>"+data.out.list[i].sale_dollar+"</td>";
								content+=    "<td>"+data.out.list[i].remark+"</td>";
								content+="</tr>";
							}
							$("#tableContent tbody").html(content);
							initPage(data.out.pageNumber, data.out.totalPage, data.out.totalRow);
							initPriceCount();
						} else {
							$("#tableContent tbody").html("<tr><td><font color='red'>暂无数据</font></td></tr>");
							$("#page").html("");
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
