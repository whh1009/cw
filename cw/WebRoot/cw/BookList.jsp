<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>${title }--图书列表</title>
    <link rel="shortcut icon" href="${ctx }/favicon.ico" />
    <script src="${ctx }/js/jquery-1.10.2.min.js" type="text/javascript"></script>
	<style>
		.cen{text-align:center;}
		table{
			font-size:90%;
		}
		.input-group, .col-lg-3 {
			margin-top:1em;
		}
	</style>
	
	
	<link href="${ctx }/css/showLoading.css" rel="stylesheet">
	<script src="${ctx }/js/jquery.showLoading.min.js"></script>
	
	<script type="text/javascript">
	
	
	
	
	
	function exportExcelData() {
		/*
		$.ajax({
			url:"${ctx}/exportDataByBookName",
			data:{searchCondition:_searchCondition, searchVal:_searchVal, userId:_userId},
			type:"post",
			dataType:"text",
			beforeSend: function () {
				$("body").showLoading();
			},
			success:function(data) {
				$("body").hideLoading();
				if(data=="") {
					alert("生成excel失败");
				} else {
					window.location.href="${ctx}/downExcel?fileName="+data;
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown) {
				console.log(XMLHttpRequest);
				console.log(textStatus);
				console.log(errorThrown);
				$("body").hideLoading();
			}
		})
		*/
	}
	
	
	
	
	
	</script>
  </head>
  
  <body>
  	<jsp:include page="_header.jsp"></jsp:include>
    <div class="container">
		<div class="row">
			<div class="col-sm-8">
				<select id="excelSel" class="form-control">
				</select>
			</div>
			<div class="col-sm-4">
    			<button type="button" class="btn btn-info" onclick="initSearch(1)"><i class='glyphicon glyphicon-search'></i> 查询</button>
    		</div>
		</div>
		<div class="row" id="demo">
    		<div class="col-sm-4">
    			<label>检索条件：</label>
    			<select class="sType0 form-control">
    			</select>
    		</div>
    		<div class="col-sm-4">
    			<label>检索值：</label>
    			<input type="text" class="form-control sVal0" placeholder="模糊检索">
    		</div>
    		<div class="col-sm-4">
    			<br />
    			<small style="cursor:pointer" data-toggle="tooltip" data-placement="right" title="添加检索条件" onclick="addSearchConditionBtn()"><i class="glyphicon glyphicon-plus"></i></small>
    		</div>
    	</div>
    	<div class="row">
    		<div class="alert alert-danger" style="float:right;">
    			<span class="sr-only">Error:</span>
    			<small id="summary"></small>
    		</div>
    	</div>
		<div class="row">
			<table class="table table-hover table-bordered" style="text-align:center">
				<thead>
					<tr>
						<th class='cen'>来源</th>
						<th class='cen'>ISBN</th>
						<th class='cen'>书名</th>
						<th class='cen'>文种</th>
						<th class='cen'>责编</th>
						<th class='cen'>EPUB价格</th>
						<th class='cen'>PDF价格</th>
						<th class='cen'>广告费</th>
						<th class='cen'>转码费</th>
						<th class='cen'>作者版税</th>
						<th class='cen'>转码时间</th>
						<th class='cen' style="width:80px">操作</th>
					</tr>
				</thead>
				<tbody id="content"></tbody>
			</table>
		</div>
		<div id="page"></div>
    </div>
    <script>
    var searchConditionJson = ${searchConditionJson};
    var conditionCount=0;
    var _page = 1;//页码
    var mySearchSql = "";
    $(function() {
    	initExcelMap();
    	$('[data-toggle="tooltip"]').tooltip();
    	initSearchSel(".sType"+conditionCount);
    })
    
    
	function initSearch(page) {
    	mySearchSql = "1=1";
    	getSearchCondition();
		_page = page;
		$.post("${ctx}/book/getBookList", {mySearchSql:mySearchSql, page:_page}, function(data) {
			var content="";
			if(data&&data.list.length) {
				for(var i = 0; i < data.list.length; i++) {
					content+="<tr>";
					content+="<td>"+data.list[i].is_self+"</td>";
					content+="<td>"+data.list[i].book_isbn+"</td>";
					content+="<td>"+data.list[i].book_name+"</td>";
					content+="<td>"+data.list[i].book_lang+"</td>";
					content+="<td>"+data.list[i].book_author+"</td>";
					content+="<td>"+data.list[i].epub_price.toFixed(2)+"</td>";
					content+="<td>"+data.list[i].pdf_price.toFixed(2)+"</td>";
					content+="<td>"+data.list[i].ad_price.toFixed(2)+"</td>";
					content+="<td>"+(data.list[i].epub_price+data.list[i].pdf_price+data.list[i].ad_price).toFixed(2)+"</td>";
					content+="<td>"+data.list[i].author_royalty.toFixed(2)+"</td>";
					content+="<td>"+data.list[i].trans_time+"</td>";
					content+="<td><a href=\"${ctx}/book/bookInfo?id="+data.list[i].id+"\" title='修改'><span class='glyphicon glyphicon-pencil' style='color:blue'></span></a>&nbsp;&nbsp;&nbsp;<a href=\"javascript:updateBook('"+data.list[i].id+"')\" title='删除'><span class='glyphicon glyphicon-trash' style='color:red'></span></a></td>";
					content+="</tr>";
				}
				initPage(data.pageNumber, data.totalPage, data.totalRow);
			}
			$("#content").html(content);
		});
		getSummary();
	}
    
    //初始化批次映射
    function initExcelMap() {
		$.ajax({
			url:"${ctx}/book/getExcelList",
			method:"POST",
			beforeSend:function() {
				$("#excelSel").html("<option value='0'>暂无数据</option>");
			},
			success:function(data) {
				$("#excelSel").html("");
				var html = "<option value='0'>按批次查询，请选择</option>";
				for(var i=0;i<data.length;i++){
					html+="<option value='"+data[i].server_excel_name.replace(".xlsx","")+"'>"+data[i].upload_excel_name+"</option>";
				}
				$("#excelSel").html(html);
			},
			error:function() {
				$("#excelSel").html("<option value='0'>暂无数据</option>");
			}
		});
	}
    
    //添加检索条件框
    function addSearchConditionBtn() {
    	conditionCount++;
    	var content = "<div class='row'>";
    	content+="<div class='col-sm-4'><select class='sType"+conditionCount+" form-control'><option value='0'>请选择</option></select></div>";
    	content+="<div class='col-sm-4'><input type='text' class='form-control sVal"+conditionCount+"' placeholder='模糊检索'></div>";
    	content+="<div class='col-sm-4'><small style='cursor:pointer' data-toggle='tooltip' data-placement='top' title='删除检索条件' onclick='removeSearchConditionBtn(this)'><i class='glyphicon glyphicon-minus'></i></small></div>";
    	content+="</div>";
    	$("#demo").after(content);
    	$('[data-toggle="tooltip"]').tooltip();
    	initSearchSel(".sType"+conditionCount);
    	
    }
    
    //删除检索条件框
    function removeSearchConditionBtn(arg) {
    	$(arg).parent().parent().remove();
    }
    
    //初始化检索条件select
    function initSearchSel(arg) {
    	if(searchConditionJson) {
    		var option = "<option value='0'>请选择</option>";
    		for(key in searchConditionJson) {
    			option+="<option value='"+key+"'>"+searchConditionJson[key]+"</option>";
    		}
    		$(arg).html(option);
    	}
    }
    
    //检索
    function getSearchCondition(){
    	var excelName = $("#excelSel").val();
    	if(excelName!=0) {
    		mySearchSql+=" and server_excel_name = "+excelName;
    	}
    	$("select[class^=sType]").each(function() {
    		var s = $(this).val();
    		var temp = $(this).attr("class").replace(" form-control","").replace("sType", "");
    		var v = $(".sVal"+temp).val();
    		if(s!="0") {
    			mySearchSql+=" and "+s+" like '%"+v+"%'";
    		}
    	});
    }
    
    //总结统计
    function getSummary() {
		$.ajax({
			url:"${ctx}/book/getSummary",
			data:{mySearchSql:mySearchSql},
			method:"POST",
			success:function(data) {
				var html = "";
				html+="EPUB总价："+data[0];
				html+="&nbsp;&nbsp;PDF总价："+data[1];
				html+="&nbsp;&nbsp;广告费："+data[2];
				html+="&nbsp;&nbsp;样章："+data[3];
				html+=" <br />其他费用1："+data[4];
				html+="&nbsp;&nbsp;其他费用2："+data[5];
				html+="&nbsp;&nbsp;其他费用3："+data[6];
				html+="&nbsp;&nbsp;其他费用4："+data[7];
				html+="&nbsp;&nbsp;其他费用5："+data[8];
				html+=" <br />总支出："+data[9];
				$("#summary").html(html);
			},
			error:function() {
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
	
	
	
	//
	function updateBook(bId) {
		if(confirm("确定要删除吗？")) {
			$.post("${ctx}/book/updateBookById", {bookId:bId}, function(data) {
				if(data=="1") {
					alert("删除成功");
					initSearch(_page);
				} else if(data=="-1") {
					alert("删除异常");
				} else if(data=="2") {
					alert("删除失败");
				} else {
					alert("未获取到图书ID");
				}
			});
		}
	}
    
    </script>
  </body>
</html>
