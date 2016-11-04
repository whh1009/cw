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
<title>${title }--销售分析</title>
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
<script src="${ctx }/plugins/echarts/echarts.min.js"></script>
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
					<label>书号：</label>
					<input type="text" class="form-control" style="width:14em;" id="bookNum" placeholder="不输入则统计全部">
				</div>
				<button class="btn btn-success" onclick="initQuery()">提交</button>
			</div>
		</div>
		<div class="row">
			<div id="pic"  style="width:100%; height:50%"></div>
		</div>
	</div>
	<script>
	var year = "";
	var date=new Date;
	year=date.getFullYear();
	
	$(function() {
		
		initYear();
		
		$("#yearSel").change(function() {
			year = $(this).val();
		});
		
		initQuery();
	});
	
	function initYear() {
		var years = "${years}";
		var yearSelHtml = "<option value=\"\">请选年份</option>";
		for(var i=0; i<years.split(",").length; i++) {
			var yy = years.split(",")[i];
			if(yy==year) {
				yearSelHtml+="<option value=\""+yy+"\" selected>"+yy+"</option>";
			} else {
				yearSelHtml+="<option value=\""+yy+"\">"+yy+"</option>";
			}
		}
		$("#yearSel").html(yearSelHtml);
	}
	
	function initQuery() {
		if(year==""||year=="0") {
			alert("请至少选择一个年份");
			return;
		}
		bookNum = $("#bookNum").val();
		$.post("${ctx}/incoming/getIncomingPicByYear", {year:year, bookNum:bookNum}, function(data) {
			if(data) {
				var totalCountArray = new Array();
				var totalRmbArray = new Array();
				var totalDollarArray = new Array();
				for(var i = 0; i < data.split("@")[0].split(",").length; i++) {
					totalCountArray.push(data.split("@")[0].split(",")[i]);
				}
				for(var i = 0; i < data.split("@")[0].split(",").length; i++) {
					totalRmbArray.push(data.split("@")[1].split(",")[i]);
				}
				for(var i = 0; i < data.split("@")[0].split(",").length; i++) {
					totalDollarArray.push(data.split("@")[2].split(",")[i]);
				}
			}
			showPic(totalCountArray, totalRmbArray, totalDollarArray);
		});
	}
	
	function showPic(totalCountArray, totalRmbArray, totalDollarArray) {
		//var myData = [{name:"销量",type:"line",stack:"总量",data:[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]},{name:"回款人民币",type:"line",stack:"总量",data:[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]},{name:"回款美元",type:"line",stack:"总量",data:[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]}];
		//基于准备好的dom，初始化echarts实例
		var myChart = echarts.init(document.getElementById("pic"));
		// 指定图表的配置项和数据
		option = {
		    title: {
		        text: "电子书销售在线统计",
		        subtext: "五洲出版社"
		    },
		    tooltip: {
		        trigger: "axis"
		    },
		    legend: {
		        data:["销量","回款人民币","回款美元"]
		    },
		    grid: {
		        left: "3%",
		        right: "4%",
		        bottom: "3%",
		        containLabel: true
		    },
		    toolbox: {
		    	show: true,
		        feature: {
		            //magicType: {show: true, type: ['stack', 'tiled']},
		            saveAsImage: {show: true}
		        }
		    },
		    xAxis: {
		        type: "category",
		        boundaryGap: false,
		        data: ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"]
		    },
		    yAxis: {
		        type: "value"
		    },
		    series: [{
		    			name:"销量",
		    			type:"line",
		    			smooth: true,
		    			data:totalCountArray
		    		},
		    		{
		    			name:"回款人民币",
		    			type:"line",
		    			smooth: true,
		    			data:totalRmbArray
		    		},{
		    			name:"回款美元",
		    			type:"line",
		    			smooth: true,
		    			data:totalDollarArray
		    		}]
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
	}
	</script>
</body>
</html>
