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

<script type="text/javascript">
$(function() {
	var data = ${result};
	var table = $("#table").DataTable({
		language: { //初始化语言
			url:"${ctx}/datatables/lanauge/lanauge.json"
		},
	    data:data,
	    columns:[{data:""}, {data:"saleTime"}, {data:"isbn"}, {data:"bookName"}, {data:"bookAuthor"}, {data:"saleCount"}, {data:"salePrice"}, {data:"platform"}],
		columnDefs: [ {
			searchable : false,
            orderable: false,
            className: "dt-body-center",
            targets:   0,
            render: function (data, type, full, meta){
                return "<input type='checkbox' name='chkbox' value='" + $("<div/>").text(full.isbn).html() + "' />";
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
			
		} else {
			console.log("选中");
		}
	});
	
	$("#form").on("submit", function(e){
		console.log(table.rows().select());  
		e.preventDefault();
	});
});

function initTable() {
	
}


</script>
</head>

<body>
	<jsp:include page="_header.jsp"></jsp:include>
	<div class="container-fluid">
		<form id="form" action="" method="POST">
		<button>保存</button>
		<table id="table" class="table order-column">
			<thead>
				<tr>
					<th class="dt-body-center"><input name="select_all" value="1" id="example-select-all" type="checkbox" /> </th>
					<th>时间</th>
					<th>ISBN</th>
					<th>书名</th>
					<th>责编</th>
					<th>数量</th>
					<th>总价</th>
					<th>平台</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
		</form>
	</div>
	

</body>
</html>
