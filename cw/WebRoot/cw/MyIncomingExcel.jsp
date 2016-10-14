<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<link rel="shortcut icon" href="${ctx }/favicon.ico" />
<!-- 
<link rel="stylesheet" type="text/css" href="${ctx }/css/jquery.dataTables.min.css"> -->
<link rel="stylesheet" type="text/css" href="http://cdn.datatables.net/plug-ins/28e7751dbec/integration/bootstrap/3/dataTables.bootstrap.css">
<link rel="stylesheet" type="text/css" href="${ctx }/datatables/select/select.bootstrap.min.css">

<script type="text/javascript" charset="utf8" src="${ctx }/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" charset="utf8" src="${ctx }/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" charset="utf8" src="http://cdn.datatables.net/plug-ins/28e7751dbec/integration/bootstrap/3/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/datatables/select/dataTables.select.min.js"></script>
<link href="${ctx }/css/showLoading.css" rel="stylesheet">
<script src="${ctx }/js/jquery.showLoading.min.js"></script>
<style>
#table{
	font-size:12px;
}
</style>

<script type="text/javascript">
$(function() {
	var data = ${result};
	if(data.indexOf("java.io.FileNotFoundException")!=-1) {
		alert("excel不存在");
		return;
	}
	
	var table = $("#table").DataTable({
		language: { //初始化语言
			url:"${ctx}/datatables/lanauge/lanauge.json"
		},
		lengthMenu : [20, 40, 60, 100, 150, 200, 300, 400, 1000, 5000],
	    data:data,
	    columns:[{data:""}, {data:"platform"}, {data:"saleTime"}, {data:"bookNum"}, {data:"bookName"}, {data:"bookAuthor"}, {data:"bookLan"}, {data:"saleCount"}, {data:"bookPrice"}, {data:"zkl"}, {data:"bookRmb"}, {data:"bookDallor"}, {data:"remark"}],
		columnDefs: [ {
			searchable : false,
            orderable: false,
            className: "dt-body-center",
            targets:   0,
            render: function (data, type, full, meta){
                return "<input type='checkbox' name='chkbox' value='" + $("<div/>").text(full.id).html() + "' />";
            }
        } ],
        order: [[ 1, "asc" ]]
	});
	
	$("#example-select-all").on("click", function(){
		if(this.checked) {
			table.rows({ "search": "applied" }).select();
		} else {
			table.rows({ "search": "applied" }).deselect();
		}
		var rows = table.rows({ "search": "applied" }).nodes();
		$("input[type='checkbox']", rows).prop("checked", this.checked);
	});

	$("#table tbody").on("change", "input[type='checkbox']", function(){
		if(!this.checked){
			var el = $("#example-select-all").get(0);
			if(el && el.checked && ("indeterminate" in el)){
				el.indeterminate = true;
			}
			$(this).parent().parent().removeClass("selected");
		} else {
			$(this).parent().parent().addClass("selected");
		}
	});
	
	$("#saveSelected").click(function() {
		var $btn = $(this).button("loading");
		if(confirm("你确定要保存吗？")) {
			if(table.rows(".selected").data().length<1) {
				alert("请至少选择一行");
			} else {
				$("#table").showLoading();
				/*
				var xml="<root name='${name}'>";
				for(var i=0;i<table.rows(".selected").data().length;i++) {
					var data = table.rows(".selected").data()[i];
					xml+="<item isbn='"+data.isbn+"' bookName='"+data.bookName.replace(/'/g, "＇")+"' bookAuthor='"+data.bookAuthor.replace(/'/g, "＇")+"' platform='"+data.platform+"' saleCount='"+data.saleCount+"' salePrice='"+data.salePrice+"' saleTime='"+data.saleTime+"' />";
				}
				xml+="</root>";
				*/
				postXml(table);
			}
		}
		$btn.button("reset");
	});
	
	$("#saveAll").on("click", function () {
		var $btn = $(this).button("loading");
		if(confirm("你确定要保存吗？")) {
			table.rows().select();
			var rows = table.rows({ "search": "applied" }).nodes();
			$("input[type='checkbox']", rows).prop("checked", true);
			if(table.rows(".selected").data().length<1) {
				alert("请至少选择一行");
			} else {
				$("#table").showLoading();
				
				postXml(table);
			}
		}
		$btn.button("reset");
	});
});

function postXml(table) {
	var xml="<root name='${name}'>";
	for(var i=0;i<table.rows(".selected").data().length;i++) {
		var data = table.rows(".selected").data()[i];
		xml+="<item platform='"+data.platform+"' saleTime='"+data.saleTime+"' bookNum='"+data.bookNum+"' bookName='"+data.bookName.replace(/'/g, "＇")+"' bookAuthor='"+data.bookAuthor.replace(/'/g, "＇")+"' bookLan='"+data.bookLan+"' saleCount='"+data.saleCount+"' bookPrice='"+data.bookPrice+"' zkl='"+data.zkl+"' bookRmb='"+data.bookRmb+"' bookDollar='"+data.bookDallor+"' />";
	}
	xml+="</root>";
	$.ajax({
		url:"${ctx }/incoming/saveInComingExcel",
		method:"post",
		dataType:"text",
		data:{xml:xml},
		success:function(data) {
			$("#table").hideLoading();
			if(data=="0"){
				alert("保存失败：未获取到excle数据");
			} else if(data=="-1"){
				alert("数据异常，不符合条件，请联系管理员");
			} else {
				alert("共保存了 "+data.split(",").length + " 条");
			}
		},
		error:function() {
			$("#table").hideLoading();
			console.log("error");
		}
	});
}

function initTable() {
	
}


</script>
</head>

<body>
	<jsp:include page="_header.jsp"></jsp:include>
	<div class="container-fluid">
		<button id="saveAll" class="btn btn-success" data-loading-text="请稍等..."  autocomplete="off">全部保存</button>
		<button id="saveSelected" class="btn btn-info" data-loading-text="请稍等..."  autocomplete="off">保存选择的行</button>
		<table id="table" class="table order-column" style="width:100%">
			<thead>
				<tr>
					<th class="dt-body-center"><input name="select_all" value="1" id="example-select-all" type="checkbox" /> </th>
					<th>账单平台</th>
					<th>账单日期</th>
					<th>书号</th>
					<th>书名</th>
					<th>作者</th>
					<th>语种</th>
					<th>销售册数</th>
					<th>定价</th>
					<th>折扣率</th>
					<th>回款人民币</th>
					<th>回款美元</th>
					<th>备注</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>
</body>
</html>
