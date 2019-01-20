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
<title>${title }--书号分类统计</title>
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
<link rel="stylesheet" type="text/css" media="all" href="${ctx }/css/daterangepicker-bs3.css" />
<link href="${ctx }/css/showLoading.css" rel="stylesheet">
<script src="${ctx }/js/jquery.showLoading.min.js"></script>
<script src="${ctx }/js/moment.js"></script>
<script src="${ctx }/js/daterangepicker.js"></script>
<script type="text/javascript">
	
</script>
</head>

<body>
	<jsp:include page="_header.jsp"></jsp:include>
	<div class="container-fluid">
		<div class="row">
			<div class="form-inline">
				<div class="form-group">
		    		<div class="input-group">
				    	<span class="input-group-addon">
				        	<label><i class="glyphicon glyphicon-calendar"></i></label>
				      	</span>
				      	<input type="text" style="width: 400px" name="reservationtime" id="reservationtime" class="form-control" value="" readonly />
		    		</div>
		    	</div>
		    	<script type="text/javascript">
					$(document).ready(function() {
						//取消readonly后退键
						$("input[readonly]").keydown(function(e) { 
							e.preventDefault();
						});
						$('#singletime').daterangepicker({
								format: 'YYYY-MM',
								singleDatePicker:true,
								startDate: moment(),
								maxDate: moment()
						});
						$('#reservationtime').daterangepicker({
								format: 'YYYY-MM',
								maxDate: moment()
							}
						);
					});
				</script>
				
				
				
			</div>
		</div>
	
		<div class="row">
			<div class="form-inline">
				
				
				
				
				<div class="form-group">
					<label>平台：</label>
					<select class="form-control" id="platformSel">
						<option value="0">请选择平台</option>
					</select>
				</div>
				<div class="form-group">
					<label>语种：</label>
					<select class="form-control" id="bookLanSel">
						<option value="0">请选择语种</option>
					</select>
				</div>
				
				<div class="form-group">
					<button class="btn btn-success" onClick="initSearch(1)"><i class="glyphicon glyphicon-search"></i>&nbsp;检索</button>
				</div>
				<div class="form-group">
					<button class="btn btn-info" id="importBtn" onClick="importData()"><i class="glyphicon glyphicon-import"></i>&nbsp;导出</button>
				</div>
				
			</div>
		</div>
	
		
		<div class="row">
			<table class="table table-hover" id="tableContent">
				<thead>
					<tr>
						<th>账单平台</th>
						<th>书号</th>
						<th>书名</th>
						<th>作者</th>
						<th>语种</th>
						<th>销售册数</th>
						
						<th>回款人民币</th>
						<th>回款美元</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
			<div id="page"></div>
		</div>
	</div>
	<script>
		var _page = 1;
		$(function() {
			initPlatform();
			initBookLan();
			initTableBody();
			
			
		});
		
		String.prototype.startWith=function(str){  
            if(str==null||str==""||this.length==0||str.length>this.length)  
              return false;  
            if(this.substr(0,str.length)==str)  
              return true;  
            else  
              return false;  
            return true;  
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
		
		//初始化表格正文
		function initTableBody() {
			initSearch(1);
		}
		var reservationTime = "";
		var startTime = "";
		var endTime = "";
		var platform = "";
		function initSearch(page) {
			reservationTime = $("#reservationtime").val();
			startTime = reservationTime.split(" \- ")[0];
			endTime = reservationTime.split(" \- ")[1];
			platform = $("#platformSel").val();
			$.ajax({
				url:"${ctx}/incoming/groupByBookNum",
				method:"POST",
				data:{page:page, startTime: startTime, endTime:endTime, platform:platform},
				beforeSend:function() {
					$("#tableContent").showLoading();
				},
				success:function(data) {
					$("#tableContent tbody").html("");
					$("#tableContent").hideLoading();
					if(data) {
						if(data.list&&data.totalRow>0) {
							var content= "";
							for(var i = 0; i<data.list.length; i++) {
								content+="<tr><td>"+data.list[i].platform+"</td>";
								content+=    "<td>"+data.list[i].book_num+"</td>";
								content+=    "<td>"+data.list[i].book_name+"</td>";
								content+=    "<td>"+data.list[i].book_author+"</td>";
								content+=    "<td>"+data.list[i].book_lan+"</td>";
								content+=    "<td>"+data.list[i].sale+"</td>";
								
								content+=    "<td>"+data.list[i].rmb+"</td>";
								content+=    "<td>"+data.list[i].dollar+"</td>";
								content+="</tr>";
							}
							$("#tableContent tbody").html(content);
							initPage(data.pageNumber, data.totalPage, data.totalRow);
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
			$.ajax({
				url:"${ctx}/incoming/exportGroupByBookNum",
				method:"POST",
				data:{startTime: startTime, endTime:endTime, platform:platform},
				beforeSend:function() {
					$("#importBtn").showLoading();
				},
				success:function(data) {
					$("#importBtn").hideLoading();
					console.log(data);
					window.location.href = "${ctx}/incoming/downXlsx?n="+data;
				},
				error:function() {
					$("#importBtn").hideLoading();
					console.log("ajax error...........")
				}
			});
		}
	</script>
</body>
</html>
