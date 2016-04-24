<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<link rel="shortcut icon" href="${ctx }/favicon.ico" />
<title>${title }</title>
<script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
<style>
</style>


<script src="${ctx }/plugins/echarts/echarts.min.js"></script>



</head>
<body>

	<jsp:include page="_header.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<div class="col-sm-6">
				<div id="main" style="width: 600px;height:400px;"></div>


			</div>
		</div>
	</div>
	<script type="text/javascript">
		//基于准备好的dom，初始化echarts实例
		//var myChart = echarts.init(document.getElementById("main"));
		// 指定图表的配置项和数据
		var option = {
			title : {
				text : 'ECharts 入门示例'
			},
			tooltip : {},
			legend : {
				data : [ '销量' ]
			},
			xAxis : {
				data : [ "衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子" ]
			},
			yAxis : {},
			series : [ {
				name : '销量',
				type : 'bar',
				data : [ 5, 20, 36, 10, 10, 20 ]
			} ]
		};

		// 使用刚指定的配置项和数据显示图表。
		//myChart.setOption(option);
	</script>
</body>
</html>
