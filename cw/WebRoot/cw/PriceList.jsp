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

.cho{
	cursor:pointer;
}
caption{
	margin-top: 15px;
    margin-bottom: 10px;
    font-size: 20px;
}
.sort{
	color:red;
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
					<label>月份：</label>
					<select class="form-control" id="monthSel">
						<option value="0">请选择月份</option>
					</select>
				</div>
				<div class="form-group">
					<label>平台：</label>
					<select class="form-control" id="platformSel">
						<option value="0">请选择平台</option>
					</select>
				</div>
				<button class="btn btn-info" onclick="initSearch('销量')">查询</button>
			</div>
		</div>
		<div class="row">
			<!-- <div class="col-sm-6"> -->
			<table class="table table-hover" id="tableContent">
				<caption>TOP 15</caption>
				<thead>
					<tr>
						<th>书号</th>
						<th>书名</th>
						<th class="cho"><span data-toggle="tooltip" data-placement="top" title="点我排序">销量</span></th>
						<th class="cho"><span data-toggle="tooltip" data-placement="top" title="点我排序">人民币</span></th>
						<th class="cho"><span data-toggle="tooltip" data-placement="top" title="点我排序">美元</span></th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
			<!-- </div> -->
		</div>
	</div>
	<script>
	var year = "";
	var month = "";
	$(function() {
		$("[data-toggle='tooltip']").tooltip()
		initYear();
		initPlatform();
		var saleTime = "${saleTime}";
		$("#yearSel").change(function() {
			year = $(this).val();
			var monthSelHtml="<option value=\"0\">请选择月份</option>";
			for(var i=0; i<saleTime.split(",").length;i++) {
				if(saleTime.split(",")[i].startWith(year)) {
					monthSelHtml+="<option value=\""+saleTime.split(",")[i].substr(4)+"\">"+saleTime.split(",")[i].substr(4)+"</option>";
				}
			}
			$("#monthSel").html(monthSelHtml);
		});
		$("#monthSel").change(function() {
			month = $(this).val();
		});
		
		$(".cho").click(function() {
			$(".cho").removeClass("sort");
			$(this).addClass("sort");
			initSearch($(this).find("span").html());
		});
	});
	
	function initYear() {
		var years = "${years}";
		var yearSelHtml = "<option value=\"\">请选年份</option>";
		for(var i=0; i<years.split(",").length; i++) {
			yearSelHtml+="<option value=\""+years.split(",")[i]+"\">"+years.split(",")[i]+"</option>";
		}
		$("#yearSel").html(yearSelHtml);
	}
	
	function initPlatform() {
		var data = eval(${platform});	
		if(data){
			var html = "<option value='0'>请选择平台</option>";
			for(var i = 0 ; i < data.length; i ++) {
				html += "<option value='"+data[i].platform+"'>"+data[i].platform+"</option>";
			}
			$("#platformSel").html(html);
		}
	}
	
	function initSearch(type) {
		if(year==""||year=="0") {
			alert("请至少选择一个年份");
			return;
		}
		if(month=="0"){
			month = "";
		}
		var platform = $("#platformSel").val();
		if(platform=="0"){
			platform = "";
		}
		$.ajax({
			url:"${ctx}/incoming/getPriceList",
			method:"POST",
			data:{year:year, month:month, type:type, platform:platform},
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
	String.prototype.startWith=function(str){  
        if(str==null||str==""||this.length==0||str.length>this.length)  
          return false;  
        if(this.substr(0,str.length)==str)  
          return true;  
        else  
          return false;  
        return true;  
    }  
			
	</script>
</body>
</html>
